# kmp-echarts-json 模块说明

ECharts 选项 JSON 的纯 KMP 模型。通用约定（版本、发布、文档、测试）在仓库根
[AGENTS.md](../AGENTS.md)，这里只记本模块特有的约束和取舍。

## 定位与平台

- 不是 npm 包的 wrapper：它不绑定任何 JS 包，只把 ECharts 的选项结构建模成 `@Serializable` 类并编码成
  JSON 字符串。所有实现位于 `commonMain`，**不要引入 JS 专属依赖**。
- 目标平台：`jvm`、`js(nodejs)`、`wasmJs(nodejs)`、`linuxX64`、`mingwX64`。Apple 目标需要 macOS 构建机，
  本仓库的 CI 跑不了，因此没有添加；需要时在 macOS 上补 `iosArm64` / `macosArm64` 等目标即可，源码无需
  改动。
- 版本基数取根 `package.json` 的 `echarts` 版本（它建模的是 ECharts 的选项结构），修订号在
  `wrapper-revisions.properties` 里独立维护，和 wrapper 模块一样。
- 新依赖必须同时提供 JVM / JS / Wasm / Native 的构件；只发 JVM 或只发 JS 的库不能加到 `commonMain`。

## 序列化约定

- 共享的 `Json` 在 `EChartsJson`：`encodeDefaults = false` + `explicitNulls = false`。ECharts 只对缺失
  字段套默认值，因此模型属性一律可空、默认 `null`；**不要把属性默认值改成非空值**，那会让字段始终被输出。
- `@Serializable` 类里出现的接口类型（`NumberOrString`、`DataValue`、`EChartsColor`、
  `LegendItem`、`DatasetSource`）必须带 `@Serializable(with = ...Serializer::class)`，否则该类型没有
  serializer，包含它的模型无法编译或无法序列化。
- 自定义 serializer 直接写 `JsonElement` 时，`explicitNulls = false` 不会生效：手工构造对象必须用
  `value?.let { put("key", it) }` 跳过 `null`，不能 `put("key", value)`（后者会写入 `JsonNull`）。
- 时间值用 kotlinx-datetime 0.8 的 `month.number`（需要 `import kotlinx.datetime.number`），旧的
  `monthNumber` 已废弃。
- `DataValue.Number`、`NumberOrString.Number`、`DataValue.Text` 这些同名嵌套类是有意的：调用方用
  `valueOf(...)`、`num(...)`、`textValue(...)` 构造，不需要写全名。

## 建模取舍

- `SeriesOption` 是所有系列族共用的扁平类：必填 `type`（`SeriesType` value class），其余按系列族分组的
  可选字段并列。ECharts 会忽略与 `type` 无关的字段。拆成 sealed 子类的代价是所有 API 要么泛型化、要么
  丢失字段，维护成本远高于收益；`SeriesType` 是 value class 也正是为了让 `echarts-gl` 等第三方系列能写。
- 单组件字段（`title`、`legend`、`xAxis` 等）一律是 `List<T>`：ECharts 接受「单个对象或数组」，建模成
  数组后两种写法都成立，也保留了多组件能力。
- 回调型属性（`formatter`、`position`、`renderItem` 等）在 JSON 里没有意义，只建模字符串/数据形态；
  函数形态由调用方在解析后自己补。
- 联合类型统一用 sealed interface + 同名工厂（`BooleanOrString.of(...)` 等），不使用 `dynamic`。
  上游本身就是 `unknown` / 任意对象的字段用 `kotlinx.serialization.json.JsonElement` / `JsonObject`。
- `EChartsOption.extra: JsonObject?` 是顶层逃生口：`@Transient`，在 `toJsonElement()` 里合并且优先于
  类型化字段。嵌套逃生口靠 `with` / `deepMerge`，不要给每个类都加 `extra`。

## DSL

- DSL 只做「追加组件」或「赋值/编辑已有对象」，不提供 `render` / `update` 之类入口。
- 嵌套 receiver 上的便捷函数必须定义在嵌套类型上：例如 `emphasis { label { } }` 里的 `label` 必须解析到
  `EmphasisOption.label`。如果只在外层 `SeriesOption` 上定义 `label`，内层调用会静默改掉外层字段（这个
  bug 已经出现过一次）。新增嵌套构造时先想清楚 receiver 的解析顺序。
- `series { }` 与 `title { }` 不同：`series` 是替换整个列表，`title` 是追加。保持这个区别，不要在
  `series` 里改成追加。

## 测试

- 测试全部在 `src/commonTest`，用 `kotlin.test`；每个目标都跑同一份用例。
- `jvmTest`、`jsNodeTest`、`wasmJsNodeTest`、`mingwX64Test` 在 Windows 主机可跑；`linuxX64Test` 只能在
  Linux 上跑（主机不匹配时 Gradle 会禁用该任务，属正常）。
- 断言不要依赖平台相关的数字格式化：`(1.0).toString()` 在 JVM 上是 `"1.0"`、在 JS 上是 `"1"`。比较
  `DataValue` / `JsonElement` 结构，或比较数值本身，不要比较序列化出来的数字文本。
- 新增模型字段或序列化器时补对应用例：键名、未设置字段被省略、联合类型两种形态、往返编解码。
- 字段级核对：`node scripts/field-coverage.mjs kmp-echarts-json`。脚本对纯 KMP 模块读
  `src/commonMain` / `src/commonTest`，列出「属性名从没在用例里出现过」的成员。当前是 0 个未点名：
  每个模型类都有 `<类型>Fields` 用例，逐个赋值后断言序列化出的键集合与字段名一致（`FieldSupport.kt`
  里的 `encodeWith`）。新增模型字段后，把字段加进对应 `*FieldsTest`；只有断言键名的那份用例能发现
  `@SerialName` 或手写 serializer 的键名漂移。
