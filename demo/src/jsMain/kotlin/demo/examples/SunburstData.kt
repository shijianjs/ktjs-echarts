package demo.examples

import echarts.options.SunburstLabelOption
import echarts.options.OptionDataValue
import echarts.options.OptionId
import echarts.options.SunburstSeriesNodeItemOption
import js.objects.unsafeJso
import zrender.ZRColor

/*
 * Data of the sunburst examples, extracted from the upstream example sources.
 *
 * The datasets are too large to be retyped, and `sunburst-book.js` derives its data with a
 * transformation loop, so both are taken from the original files. Each function builds a fresh node
 * tree, so every mounted chart owns its own data.
 *
 * `downplay` is not carried over: ECharts 6 replaced that state with `blur`, and the upstream
 * example still sets it on the labels of `sunburst-book`.
 */

/** One sunburst node: name, value, an explicit colour and an optional label colour. */
internal fun sunburstNode(
    name: String? = null,
    value: Double? = null,
    color: String? = null,
    alpha: Double? = null,
    labelColor: String? = null,
    children: Array<SunburstSeriesNodeItemOption>? = null,
): SunburstSeriesNodeItemOption {
    // The member names of the object would shadow the parameters inside unsafeJso, so the
    // arguments are copied to locals first.
    val nodeName = name
    val nodeValue = value
    val fill = color
    val alphaValue = alpha
    val textColor = labelColor
    val nodeChildren = children
    return unsafeJso {
        if (nodeName != null) this.name = OptionId(nodeName)
        if (nodeValue != null) this.value = OptionDataValue(nodeValue)
        if (fill != null || alphaValue != null) {
            itemStyle =
                unsafeJso {
                    if (fill != null) this.color = ZRColor(fill)
                    this.opacity = alphaValue
                }
        }
        if (textColor != null) {
            label = unsafeJso<SunburstLabelOption> { this.color = textColor }
        }
        this.children = nodeChildren
    }
}

/** `familyData` of the official example of the same name. */
internal fun familyData(): Array<SunburstSeriesNodeItemOption?> =
    arrayOf(
        sunburstNode(
            "Grandpa",
            children =
                arrayOf(
                    sunburstNode(
                        "Uncle Leo",
                        value = 15.0,
                        children =
                            arrayOf(
                                sunburstNode("Cousin Jack", value = 2.0),
                                sunburstNode(
                                    "Cousin Mary",
                                    value = 5.0,
                                    children =
                                        arrayOf(
                                            sunburstNode("Jackson", value = 2.0),
                                        ),
                                ),
                                sunburstNode("Cousin Ben", value = 4.0),
                            ),
                    ),
                    sunburstNode(
                        "Father",
                        value = 10.0,
                        children =
                            arrayOf(
                                sunburstNode("Me", value = 5.0),
                                sunburstNode("Brother Peter", value = 1.0),
                            ),
                    ),
                ),
        ),
        sunburstNode(
            "Nancy",
            children =
                arrayOf(
                    sunburstNode(
                        "Uncle Nike",
                        children =
                            arrayOf(
                                sunburstNode("Cousin Betty", value = 1.0),
                                sunburstNode("Cousin Jenny", value = 2.0),
                            ),
                    ),
                ),
        ),
    )

/** `visualMapData` of the official example of the same name. */
internal fun visualMapData(): Array<SunburstSeriesNodeItemOption?> =
    arrayOf(
        sunburstNode(
            "Grandpa",
            children =
                arrayOf(
                    sunburstNode(
                        "Uncle Leo",
                        value = 15.0,
                        children =
                            arrayOf(
                                sunburstNode("Cousin Jack", value = 2.0),
                                sunburstNode(
                                    "Cousin Mary",
                                    value = 5.0,
                                    children =
                                        arrayOf(
                                            sunburstNode("Jackson", value = 2.0),
                                        ),
                                ),
                                sunburstNode("Cousin Ben", value = 4.0),
                            ),
                    ),
                    sunburstNode(
                        "Aunt Jane",
                        children =
                            arrayOf(
                                sunburstNode("Cousin Kate", value = 4.0),
                            ),
                    ),
                    sunburstNode(
                        "Father",
                        value = 10.0,
                        children =
                            arrayOf(
                                sunburstNode("Me", value = 5.0, color = "red"),
                                sunburstNode("Brother Peter", value = 1.0),
                            ),
                    ),
                ),
        ),
        sunburstNode(
            "Mike",
            children =
                arrayOf(
                    sunburstNode(
                        "Uncle Dan",
                        children =
                            arrayOf(
                                sunburstNode("Cousin Lucy", value = 3.0),
                                sunburstNode(
                                    "Cousin Luck",
                                    value = 4.0,
                                    children =
                                        arrayOf(
                                            sunburstNode("Nephew", value = 2.0),
                                        ),
                                ),
                            ),
                    ),
                ),
        ),
        sunburstNode(
            "Nancy",
            children =
                arrayOf(
                    sunburstNode(
                        "Uncle Nike",
                        children =
                            arrayOf(
                                sunburstNode("Cousin Betty", value = 1.0),
                                sunburstNode("Cousin Jenny", value = 2.0),
                            ),
                    ),
                ),
        ),
    )

/** `drinkData` of the official example of the same name. */
internal fun drinkData(): Array<SunburstSeriesNodeItemOption?> =
    arrayOf(
        sunburstNode(
            "Flora",
            color = "#da0d68",
            children =
                arrayOf(
                    sunburstNode("Black Tea", value = 1.0, color = "#975e6d"),
                    sunburstNode(
                        "Floral",
                        color = "#e0719c",
                        children =
                            arrayOf(
                                sunburstNode("Chamomile", value = 1.0, color = "#f99e1c"),
                                sunburstNode("Rose", value = 1.0, color = "#ef5a78"),
                                sunburstNode("Jasmine", value = 1.0, color = "#f7f1bd"),
                            ),
                    ),
                ),
        ),
        sunburstNode(
            "Fruity",
            color = "#da1d23",
            children =
                arrayOf(
                    sunburstNode(
                        "Berry",
                        color = "#dd4c51",
                        children =
                            arrayOf(
                                sunburstNode("Blackberry", value = 1.0, color = "#3e0317"),
                                sunburstNode("Raspberry", value = 1.0, color = "#e62969"),
                                sunburstNode("Blueberry", value = 1.0, color = "#6569b0"),
                                sunburstNode("Strawberry", value = 1.0, color = "#ef2d36"),
                            ),
                    ),
                    sunburstNode(
                        "Dried Fruit",
                        color = "#c94a44",
                        children =
                            arrayOf(
                                sunburstNode("Raisin", value = 1.0, color = "#b53b54"),
                                sunburstNode("Prune", value = 1.0, color = "#a5446f"),
                            ),
                    ),
                    sunburstNode(
                        "Other Fruit",
                        color = "#dd4c51",
                        children =
                            arrayOf(
                                sunburstNode("Coconut", value = 1.0, color = "#f2684b"),
                                sunburstNode("Cherry", value = 1.0, color = "#e73451"),
                                sunburstNode("Pomegranate", value = 1.0, color = "#e65656"),
                                sunburstNode("Pineapple", value = 1.0, color = "#f89a1c"),
                                sunburstNode("Grape", value = 1.0, color = "#aeb92c"),
                                sunburstNode("Apple", value = 1.0, color = "#4eb849"),
                                sunburstNode("Peach", value = 1.0, color = "#f68a5c"),
                                sunburstNode("Pear", value = 1.0, color = "#baa635"),
                            ),
                    ),
                    sunburstNode(
                        "Citrus Fruit",
                        color = "#f7a128",
                        children =
                            arrayOf(
                                sunburstNode("Grapefruit", value = 1.0, color = "#f26355"),
                                sunburstNode("Orange", value = 1.0, color = "#e2631e"),
                                sunburstNode("Lemon", value = 1.0, color = "#fde404"),
                                sunburstNode("Lime", value = 1.0, color = "#7eb138"),
                            ),
                    ),
                ),
        ),
        sunburstNode(
            "Sour/\nFermented",
            color = "#ebb40f",
            children =
                arrayOf(
                    sunburstNode(
                        "Sour",
                        color = "#e1c315",
                        children =
                            arrayOf(
                                sunburstNode("Sour Aromatics", value = 1.0, color = "#9ea718"),
                                sunburstNode("Acetic Acid", value = 1.0, color = "#94a76f"),
                                sunburstNode("Butyric Acid", value = 1.0, color = "#d0b24f"),
                                sunburstNode("Isovaleric Acid", value = 1.0, color = "#8eb646"),
                                sunburstNode("Citric Acid", value = 1.0, color = "#faef07"),
                                sunburstNode("Malic Acid", value = 1.0, color = "#c1ba07"),
                            ),
                    ),
                    sunburstNode(
                        "Alcohol/\nFremented",
                        color = "#b09733",
                        children =
                            arrayOf(
                                sunburstNode("Winey", value = 1.0, color = "#8f1c53"),
                                sunburstNode("Whiskey", value = 1.0, color = "#b34039"),
                                sunburstNode("Fremented", value = 1.0, color = "#ba9232"),
                                sunburstNode("Overripe", value = 1.0, color = "#8b6439"),
                            ),
                    ),
                ),
        ),
        sunburstNode(
            "Green/\nVegetative",
            color = "#187a2f",
            children =
                arrayOf(
                    sunburstNode("Olive Oil", value = 1.0, color = "#a2b029"),
                    sunburstNode("Raw", value = 1.0, color = "#718933"),
                    sunburstNode(
                        "Green/\nVegetative",
                        color = "#3aa255",
                        children =
                            arrayOf(
                                sunburstNode("Under-ripe", value = 1.0, color = "#a2bb2b"),
                                sunburstNode("Peapod", value = 1.0, color = "#62aa3c"),
                                sunburstNode("Fresh", value = 1.0, color = "#03a653"),
                                sunburstNode("Dark Green", value = 1.0, color = "#038549"),
                                sunburstNode("Vegetative", value = 1.0, color = "#28b44b"),
                                sunburstNode("Hay-like", value = 1.0, color = "#a3a830"),
                                sunburstNode("Herb-like", value = 1.0, color = "#7ac141"),
                            ),
                    ),
                    sunburstNode("Beany", value = 1.0, color = "#5e9a80"),
                ),
        ),
        sunburstNode(
            "Other",
            color = "#0aa3b5",
            children =
                arrayOf(
                    sunburstNode(
                        "Papery/Musty",
                        color = "#9db2b7",
                        children =
                            arrayOf(
                                sunburstNode("Stale", value = 1.0, color = "#8b8c90"),
                                sunburstNode("Cardboard", value = 1.0, color = "#beb276"),
                                sunburstNode("Papery", value = 1.0, color = "#fefef4"),
                                sunburstNode("Woody", value = 1.0, color = "#744e03"),
                                sunburstNode("Moldy/Damp", value = 1.0, color = "#a3a36f"),
                                sunburstNode("Musty/Dusty", value = 1.0, color = "#c9b583"),
                                sunburstNode("Musty/Earthy", value = 1.0, color = "#978847"),
                                sunburstNode("Animalic", value = 1.0, color = "#9d977f"),
                                sunburstNode("Meaty Brothy", value = 1.0, color = "#cc7b6a"),
                                sunburstNode("Phenolic", value = 1.0, color = "#db646a"),
                            ),
                    ),
                    sunburstNode(
                        "Chemical",
                        color = "#76c0cb",
                        children =
                            arrayOf(
                                sunburstNode("Bitter", value = 1.0, color = "#80a89d"),
                                sunburstNode("Salty", value = 1.0, color = "#def2fd"),
                                sunburstNode("Medicinal", value = 1.0, color = "#7a9bae"),
                                sunburstNode("Petroleum", value = 1.0, color = "#039fb8"),
                                sunburstNode("Skunky", value = 1.0, color = "#5e777b"),
                                sunburstNode("Rubber", value = 1.0, color = "#120c0c"),
                            ),
                    ),
                ),
        ),
        sunburstNode(
            "Roasted",
            color = "#c94930",
            children =
                arrayOf(
                    sunburstNode("Pipe Tobacco", value = 1.0, color = "#caa465"),
                    sunburstNode("Tobacco", value = 1.0, color = "#dfbd7e"),
                    sunburstNode(
                        "Burnt",
                        color = "#be8663",
                        children =
                            arrayOf(
                                sunburstNode("Acrid", value = 1.0, color = "#b9a449"),
                                sunburstNode("Ashy", value = 1.0, color = "#899893"),
                                sunburstNode("Smoky", value = 1.0, color = "#a1743b"),
                                sunburstNode("Brown, Roast", value = 1.0, color = "#894810"),
                            ),
                    ),
                    sunburstNode(
                        "Cereal",
                        color = "#ddaf61",
                        children =
                            arrayOf(
                                sunburstNode("Grain", value = 1.0, color = "#b7906f"),
                                sunburstNode("Malt", value = 1.0, color = "#eb9d5f"),
                            ),
                    ),
                ),
        ),
        sunburstNode(
            "Spices",
            color = "#ad213e",
            children =
                arrayOf(
                    sunburstNode("Pungent", value = 1.0, color = "#794752"),
                    sunburstNode("Pepper", value = 1.0, color = "#cc3d41"),
                    sunburstNode(
                        "Brown Spice",
                        color = "#b14d57",
                        children =
                            arrayOf(
                                sunburstNode("Anise", value = 1.0, color = "#c78936"),
                                sunburstNode("Nutmeg", value = 1.0, color = "#8c292c"),
                                sunburstNode("Cinnamon", value = 1.0, color = "#e5762e"),
                                sunburstNode("Clove", value = 1.0, color = "#a16c5a"),
                            ),
                    ),
                ),
        ),
        sunburstNode(
            "Nutty/\nCocoa",
            color = "#a87b64",
            children =
                arrayOf(
                    sunburstNode(
                        "Nutty",
                        color = "#c78869",
                        children =
                            arrayOf(
                                sunburstNode("Peanuts", value = 1.0, color = "#d4ad12"),
                                sunburstNode("Hazelnut", value = 1.0, color = "#9d5433"),
                                sunburstNode("Almond", value = 1.0, color = "#c89f83"),
                            ),
                    ),
                    sunburstNode(
                        "Cocoa",
                        color = "#bb764c",
                        children =
                            arrayOf(
                                sunburstNode("Chocolate", value = 1.0, color = "#692a19"),
                                sunburstNode("Dark Chocolate", value = 1.0, color = "#470604"),
                            ),
                    ),
                ),
        ),
        sunburstNode(
            "Sweet",
            color = "#e65832",
            children =
                arrayOf(
                    sunburstNode(
                        "Brown Sugar",
                        color = "#d45a59",
                        children =
                            arrayOf(
                                sunburstNode("Molasses", value = 1.0, color = "#310d0f"),
                                sunburstNode("Maple Syrup", value = 1.0, color = "#ae341f"),
                                sunburstNode("Caramelized", value = 1.0, color = "#d78823"),
                                sunburstNode("Honey", value = 1.0, color = "#da5c1f"),
                            ),
                    ),
                    sunburstNode("Vanilla", value = 1.0, color = "#f89a80"),
                    sunburstNode("Vanillin", value = 1.0, color = "#f37674"),
                    sunburstNode("Overall Sweet", value = 1.0, color = "#e75b68"),
                    sunburstNode("Sweet Aromatics", value = 1.0, color = "#d0545f"),
                ),
        ),
    )

/** `bookData` of the official example of the same name. */
internal fun bookData(): Array<SunburstSeriesNodeItemOption?> =
    arrayOf(
        sunburstNode(
            "虚构",
            color = "#FF7853",
            children =
                arrayOf(
                    sunburstNode(
                        "小说",
                        color = "#FF7853",
                        children =
                            arrayOf(
                                sunburstNode(
                                    "5☆",
                                    labelColor = "#FFAE57",
                                    children =
                                        arrayOf(
                                            sunburstNode("疼", value = 1.0, color = "#FFAE57", alpha = 1.0, labelColor = "#FFAE57"),
                                            sunburstNode("慈悲", value = 1.0, color = "#FFAE57", alpha = 1.0, labelColor = "#FFAE57"),
                                            sunburstNode("楼下的房客", value = 1.0, color = "#FFAE57", alpha = 1.0, labelColor = "#FFAE57"),
                                        ),
                                ),
                                sunburstNode(
                                    "4☆",
                                    labelColor = "#FF7853",
                                    children =
                                        arrayOf(
                                            sunburstNode("虚无的十字架", value = 1.0, color = "#FF7853", alpha = 1.0, labelColor = "#FF7853"),
                                            sunburstNode("无声告白", value = 1.0, color = "#FF7853", alpha = 1.0, labelColor = "#FF7853"),
                                            sunburstNode("童年的终结", value = 1.0, color = "#FF7853", alpha = 1.0, labelColor = "#FF7853"),
                                        ),
                                ),
                                sunburstNode(
                                    "3☆",
                                    labelColor = "#EA5151",
                                    children =
                                        arrayOf(
                                            sunburstNode("疯癫老人日记", value = 1.0, color = "#EA5151", alpha = 1.0, labelColor = "#EA5151"),
                                        ),
                                ),
                            ),
                    ),
                    sunburstNode(
                        "其他",
                        color = "#FF7853",
                        children =
                            arrayOf(
                                sunburstNode(
                                    "5☆",
                                    labelColor = "#FFAE57",
                                    children =
                                        arrayOf(
                                            sunburstNode("纳博科夫短篇小说全集", value = 1.0, color = "#FFAE57", alpha = 1.0, labelColor = "#FFAE57"),
                                        ),
                                ),
                                sunburstNode(
                                    "4☆",
                                    labelColor = "#FF7853",
                                    children =
                                        arrayOf(
                                            sunburstNode("安魂曲", value = 1.0, color = "#FF7853", alpha = 1.0, labelColor = "#FF7853"),
                                            sunburstNode("人生拼图版", value = 1.0, color = "#FF7853", alpha = 1.0, labelColor = "#FF7853"),
                                        ),
                                ),
                                sunburstNode(
                                    "3☆",
                                    labelColor = "#EA5151",
                                    children =
                                        arrayOf(
                                            sunburstNode("比起爱你，我更需要你", value = 1.0, color = "#EA5151", alpha = 1.0, labelColor = "#EA5151"),
                                        ),
                                ),
                            ),
                    ),
                ),
        ),
        sunburstNode(
            "非虚构",
            color = "#EA5151",
            children =
                arrayOf(
                    sunburstNode(
                        "设计",
                        color = "#EA5151",
                        children =
                            arrayOf(
                                sunburstNode(
                                    "5☆",
                                    labelColor = "#FFAE57",
                                    children =
                                        arrayOf(
                                            sunburstNode("无界面交互", value = 1.0, color = "#FFAE57", alpha = 1.0, labelColor = "#FFAE57"),
                                        ),
                                ),
                                sunburstNode(
                                    "4☆",
                                    labelColor = "#FF7853",
                                    children =
                                        arrayOf(
                                            sunburstNode("数字绘图的光照与渲染技术", value = 1.0, color = "#FF7853", alpha = 1.0, labelColor = "#FF7853"),
                                            sunburstNode("日本建筑解剖书", value = 1.0, color = "#FF7853", alpha = 1.0, labelColor = "#FF7853"),
                                        ),
                                ),
                                sunburstNode(
                                    "3☆",
                                    labelColor = "#EA5151",
                                    children =
                                        arrayOf(
                                            sunburstNode("奇幻世界艺术\n&RPG地图绘制讲座", value = 1.0, color = "#EA5151", alpha = 1.0, labelColor = "#EA5151"),
                                        ),
                                ),
                            ),
                    ),
                    sunburstNode(
                        "社科",
                        color = "#EA5151",
                        children =
                            arrayOf(
                                sunburstNode(
                                    "5☆",
                                    labelColor = "#FFAE57",
                                    children =
                                        arrayOf(
                                            sunburstNode("痛点", value = 1.0, color = "#FFAE57", alpha = 1.0, labelColor = "#FFAE57"),
                                        ),
                                ),
                                sunburstNode(
                                    "4☆",
                                    labelColor = "#FF7853",
                                    children =
                                        arrayOf(
                                            sunburstNode("卓有成效的管理者", value = 1.0, color = "#FF7853", alpha = 1.0, labelColor = "#FF7853"),
                                            sunburstNode("进化", value = 1.0, color = "#FF7853", alpha = 1.0, labelColor = "#FF7853"),
                                            sunburstNode("后物欲时代的来临", value = 1.0, color = "#FF7853", alpha = 1.0, labelColor = "#FF7853"),
                                        ),
                                ),
                                sunburstNode(
                                    "3☆",
                                    labelColor = "#EA5151",
                                    children =
                                        arrayOf(
                                            sunburstNode("疯癫与文明", value = 1.0, color = "#EA5151", alpha = 1.0, labelColor = "#EA5151"),
                                        ),
                                ),
                            ),
                    ),
                    sunburstNode(
                        "心理",
                        color = "#EA5151",
                        children =
                            arrayOf(
                                sunburstNode(
                                    "5☆",
                                    labelColor = "#FFAE57",
                                    children =
                                        arrayOf(
                                            sunburstNode("我们时代的神经症人格", value = 1.0, color = "#FFAE57", alpha = 1.0, labelColor = "#FFAE57"),
                                        ),
                                ),
                                sunburstNode(
                                    "4☆",
                                    labelColor = "#FF7853",
                                    children =
                                        arrayOf(
                                            sunburstNode("皮格马利翁效应", value = 1.0, color = "#FF7853", alpha = 1.0, labelColor = "#FF7853"),
                                            sunburstNode("受伤的人", value = 1.0, color = "#FF7853", alpha = 1.0, labelColor = "#FF7853"),
                                        ),
                                ),
                                sunburstNode("3☆", labelColor = "#EA5151"),
                                sunburstNode(
                                    "2☆",
                                    labelColor = "#CC3F57",
                                    children =
                                        arrayOf(
                                            sunburstNode("迷恋", value = 1.0, color = "#CC3F57", alpha = 1.0, labelColor = "#CC3F57"),
                                        ),
                                ),
                            ),
                    ),
                    sunburstNode(
                        "居家",
                        color = "#EA5151",
                        children =
                            arrayOf(
                                sunburstNode(
                                    "4☆",
                                    labelColor = "#FF7853",
                                    children =
                                        arrayOf(
                                            sunburstNode("把房子住成家", value = 1.0, color = "#FF7853", alpha = 1.0, labelColor = "#FF7853"),
                                            sunburstNode("只过必要生活", value = 1.0, color = "#FF7853", alpha = 1.0, labelColor = "#FF7853"),
                                            sunburstNode("北欧简约风格", value = 1.0, color = "#FF7853", alpha = 1.0, labelColor = "#FF7853"),
                                        ),
                                ),
                            ),
                    ),
                    sunburstNode(
                        "绘本",
                        color = "#EA5151",
                        children =
                            arrayOf(
                                sunburstNode(
                                    "5☆",
                                    labelColor = "#FFAE57",
                                    children =
                                        arrayOf(
                                            sunburstNode("设计诗", value = 1.0, color = "#FFAE57", alpha = 1.0, labelColor = "#FFAE57"),
                                        ),
                                ),
                                sunburstNode(
                                    "4☆",
                                    labelColor = "#FF7853",
                                    children =
                                        arrayOf(
                                            sunburstNode("假如生活糊弄了你", value = 1.0, color = "#FF7853", alpha = 1.0, labelColor = "#FF7853"),
                                            sunburstNode("博物学家的神秘动物图鉴", value = 1.0, color = "#FF7853", alpha = 1.0, labelColor = "#FF7853"),
                                        ),
                                ),
                                sunburstNode(
                                    "3☆",
                                    labelColor = "#EA5151",
                                    children =
                                        arrayOf(
                                            sunburstNode("方向", value = 1.0, color = "#EA5151", alpha = 1.0, labelColor = "#EA5151"),
                                        ),
                                ),
                            ),
                    ),
                    sunburstNode(
                        "哲学",
                        color = "#EA5151",
                        children =
                            arrayOf(
                                sunburstNode(
                                    "4☆",
                                    labelColor = "#FF7853",
                                    children =
                                        arrayOf(
                                            sunburstNode("人生的智慧", value = 1.0, color = "#FF7853", alpha = 1.0, labelColor = "#FF7853"),
                                        ),
                                ),
                            ),
                    ),
                    sunburstNode(
                        "技术",
                        color = "#EA5151",
                        children =
                            arrayOf(
                                sunburstNode(
                                    "5☆",
                                    labelColor = "#FFAE57",
                                    children =
                                        arrayOf(
                                            sunburstNode("代码整洁之道", value = 1.0, color = "#FFAE57", alpha = 1.0, labelColor = "#FFAE57"),
                                        ),
                                ),
                                sunburstNode(
                                    "4☆",
                                    labelColor = "#FF7853",
                                    children =
                                        arrayOf(
                                            sunburstNode("Three.js 开发指南", value = 1.0, color = "#FF7853", alpha = 1.0, labelColor = "#FF7853"),
                                        ),
                                ),
                            ),
                    ),
                ),
        ),
    )

/** `monochromeData` of the official example of the same name. */
internal fun monochromeData(): Array<SunburstSeriesNodeItemOption?> =
    arrayOf(
        sunburstNode(
            color = "#F54F4A",
            children =
                arrayOf(
                    sunburstNode(
                        value = 5.0,
                        color = "#F54F4A",
                        children =
                            arrayOf(
                                sunburstNode(value = 1.0, color = "#F54F4A"),
                                sunburstNode(
                                    value = 2.0,
                                    children =
                                        arrayOf(
                                            sunburstNode(value = 1.0, color = "#FF8C75"),
                                        ),
                                ),
                                sunburstNode(
                                    children =
                                        arrayOf(
                                            sunburstNode(value = 1.0),
                                        ),
                                ),
                            ),
                    ),
                    sunburstNode(
                        value = 10.0,
                        color = "#F54F4A",
                        children =
                            arrayOf(
                                sunburstNode(
                                    value = 6.0,
                                    color = "#FFB499",
                                    children =
                                        arrayOf(
                                            sunburstNode(value = 1.0, color = "#F54F4A"),
                                            sunburstNode(value = 1.0),
                                            sunburstNode(value = 1.0, color = "#FF8C75"),
                                            sunburstNode(value = 1.0),
                                        ),
                                ),
                                sunburstNode(
                                    value = 2.0,
                                    color = "#FFB499",
                                    children =
                                        arrayOf(
                                            sunburstNode(value = 1.0),
                                        ),
                                ),
                                sunburstNode(
                                    children =
                                        arrayOf(
                                            sunburstNode(value = 1.0, color = "#FF8C75"),
                                        ),
                                ),
                            ),
                    ),
                ),
        ),
        sunburstNode(
            value = 9.0,
            color = "#FF8C75",
            children =
                arrayOf(
                    sunburstNode(
                        value = 4.0,
                        color = "#F54F4A",
                        children =
                            arrayOf(
                                sunburstNode(value = 2.0, color = "#FF8C75"),
                                sunburstNode(
                                    children =
                                        arrayOf(
                                            sunburstNode(value = 1.0, color = "#F54F4A"),
                                        ),
                                ),
                            ),
                    ),
                    sunburstNode(
                        color = "#FFB499",
                        children =
                            arrayOf(
                                sunburstNode(
                                    value = 3.0,
                                    children =
                                        arrayOf(
                                            sunburstNode(value = 1.0),
                                            sunburstNode(value = 1.0, color = "#FF8C75"),
                                        ),
                                ),
                            ),
                    ),
                ),
        ),
        sunburstNode(
            value = 7.0,
            color = "#F54F4A",
            children =
                arrayOf(
                    sunburstNode(
                        color = "#FFB499",
                        children =
                            arrayOf(
                                sunburstNode(value = 1.0, color = "#FFB499"),
                                sunburstNode(
                                    value = 3.0,
                                    color = "#FF8C75",
                                    children =
                                        arrayOf(
                                            sunburstNode(value = 1.0, color = "#FF8C75"),
                                            sunburstNode(value = 1.0),
                                        ),
                                ),
                                sunburstNode(
                                    value = 2.0,
                                    color = "#F54F4A",
                                    children =
                                        arrayOf(
                                            sunburstNode(value = 1.0),
                                            sunburstNode(value = 1.0, color = "#F54F4A"),
                                        ),
                                ),
                            ),
                    ),
                ),
        ),
        sunburstNode(
            color = "#F54F4A",
            children =
                arrayOf(
                    sunburstNode(
                        value = 6.0,
                        color = "#FFB499",
                        children =
                            arrayOf(
                                sunburstNode(value = 1.0, color = "#FF8C75"),
                                sunburstNode(
                                    value = 2.0,
                                    color = "#F54F4A",
                                    children =
                                        arrayOf(
                                            sunburstNode(value = 2.0, color = "#FF8C75"),
                                        ),
                                ),
                                sunburstNode(value = 1.0, color = "#FFB499"),
                            ),
                    ),
                    sunburstNode(
                        value = 3.0,
                        color = "#FFB499",
                        children =
                            arrayOf(
                                sunburstNode(value = 1.0),
                                sunburstNode(
                                    children =
                                        arrayOf(
                                            sunburstNode(value = 1.0, color = "#FF8C75"),
                                        ),
                                ),
                                sunburstNode(value = 1.0),
                            ),
                    ),
                ),
        ),
    )

/** `labelRotateData` of the official example of the same name. */
internal fun labelRotateData(): Array<SunburstSeriesNodeItemOption?> =
    arrayOf(
        sunburstNode(
            value = 8.0,
            children =
                arrayOf(
                    sunburstNode(
                        value = 4.0,
                        children =
                            arrayOf(
                                sunburstNode(value = 2.0),
                                sunburstNode(value = 1.0),
                                sunburstNode(value = 1.0),
                                sunburstNode(value = 0.5),
                            ),
                    ),
                    sunburstNode(value = 2.0),
                ),
        ),
        sunburstNode(
            value = 4.0,
            children =
                arrayOf(
                    sunburstNode(
                        children =
                            arrayOf(
                                sunburstNode(value = 2.0),
                            ),
                    ),
                ),
        ),
        sunburstNode(
            value = 4.0,
            children =
                arrayOf(
                    sunburstNode(
                        children =
                            arrayOf(
                                sunburstNode(value = 2.0),
                            ),
                    ),
                ),
        ),
        sunburstNode(
            value = 3.0,
            children =
                arrayOf(
                    sunburstNode(
                        children =
                            arrayOf(
                                sunburstNode(value = 1.0),
                            ),
                    ),
                ),
        ),
    )
