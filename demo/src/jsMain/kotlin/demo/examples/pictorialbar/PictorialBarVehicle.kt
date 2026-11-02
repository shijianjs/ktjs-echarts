package demo.examples.pictorialbar

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import demo.examples.Category

private val pictorialBarVehicleCategory = Category(Groups.PICTORIAL_BAR, "examples/pictorialbar/PictorialBarVehicle.kt")

val pictorialBarVehicleEntry: CatalogEntry =
    pictorialBarVehicleCategory.chart(
            "pictorialBar-vehicle",
            "交通工具",
            "Vehicles",
            "两条系列的柱子用驯鹿、轮船、飞机、高铁、汽车的 `path://` 剪影重复填充，" +
                "`symbolRepeat` 让剪影沿柱子平铺，2015 与 2016 两组柱子并排比较。",
            "Two series whose bars are tiled with the `path://` silhouettes of a reindeer, a ship, a " +
                "plane, a train and a car; `symbolRepeat` lays the symbols along the bar.",
        ) {
            vehicleOption(
                arrayOf(
                    vehicleSeries("2015", VEHICLE_2015, null, StringOrDouble("40%")),
                    vehicleSeries("2016", VEHICLE_2016, StringOrDouble("10%"), null),
                ),
            )
        }
