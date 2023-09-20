PoolMate
--------
>Open source pool management app using ScalaFX, H2 and Scala 3.

Warning
-------
>Slick support for Scala 3 is still a **WIP**. Switch back to Scala 2.13.12 and Slick 3.4.1 if you require working Slick code. :)

Object Model
------------
* Pool(id, built, gallons, street, city, state, zip)
* Owner(id, poolId, since, first, last, email)
* Surface(id, poolId, installed, kind)
* Pump(id, poolId, installed, model)
* Timer(id, poolId, installed, model)
* Heater(id, poolId, installed, model)
* Lifecycle(id, poolId, created, active, pumpOn, pumpOff)
* Cleaning(id, poolId, on, deck, brush, vacuum, net, skimmerBasket, pumpBasket, pumpFilter)
* Measurement(id, poolId, on, temp, hardness, totalChlorine, bromine, freeChlorine, pH, alkalinity, cyanuricAcid)
* Additive(id, poolId, on, chemical, unit, amount)
* Supply(id, poolId, purchased, item, unit, amount, cost)
* Repair(id, poolId, on, repair, cost)

Relational Model
----------------
* Pool 1 ---> * Owner | Surface | Pump | Timer | Heater | Lifecycle | Cleaning | Measurement | Additive | Supply | Repair

Panes
-----
* east pane - supplies, lifecycles, cleanings, measurements, additives
* west pane - pools, owners, surfaces, pumps, timers, heaters, repairs

Charts
------
1. supplies - bar chart ( x = purchased, y = cost, c = item )
2. measurements - line chart ( x = on, y = chemical ** )
3. additives - bar chart ( x = on, y = amount, c = chemical )
4. repairs - line chart ( x = on, y = cost )

** a line chart for each measured chemical, to include temp

Measurements
------------
>Measured in ppm ( parts per million ).

| Measurement                       | Range     | Good        | Ideal |
|-----------------------------------|-----------|-------------|-------|
| total chlorine (tc = fc + cc)     | 0 - 10    | 1 - 5       | 3     |
| free chlorine (fc)                | 0 - 10    | 1 - 5       | 3     |
| combinded chlorine (cc = tc - fc) | 0.0 - 0.5 | 0.0 - 0.2   | 0.0   |
| ph                                | 6.2 - 8.4 | 7.2 - 7.6   | 7.4   |
| calcium hardness                  | 0 - 1000  | 250 - 500   | 375   |
| total alkalinity                  | 0 - 240   | 80 - 120    | 100   |
| cyanuric acid                     | 0 - 300   | 30 - 100    | 50    |
| total bromine                     | 0 - 20    | 2 - 10      | 5     |
| salt                              | 0 - 3600  | 2700 - 3400 | 3200  |
| temperature                       | 50 - 100  | 75 - 85     | 82    |
 
** Units of Measure - oz, gl, lb

Chemicals
---------
* Liquids measured in: gallons ( gl ) and liters ( l ).
* Granules measured in: pounds ( lbs ) and kilograms ( kg ).
1. LiquidChlorine ( gl/l )
2. Trichlor ( tablet )
3. Dichlor ( lbs/kg )
4. CalciumHypochlorite ( lbs/kg )
5. Stabilizer ( lbs/kg )
6. Algaecide ( gl/l )
7. MuriaticAcid ( gl/l )
8. Salt ( lbs/kg )

Solutions
---------
>Suggested solutions to chemical imbalances.
1. high ph - Sodium Bisulfate
2. low ph - Sodium Carbonate, Soda Ash
3. high alkalinity - Muriatic Acid, Sodium Bisulfate
4. low alkalinity - Sodium Bicarbonate, Baking Soda
5. calcium hardness - Calcium Chloride
6. low chlorine - Chlorine Tablets, Granules, Liquid
7. algae - Algaecide, Shock
8. stains - Stain Identification Kit, Stain Remover

Todo
----
1. Design and build company, worker and routing panels and dialogs.

Test
----
1. sbt clean test

Run
---
1. sbt run

Package
-------
1. sbt clean test universal:packageBin
2. verify ./target/universal/poolmate-${version}.zip

Install
-------
1. unzip ./target/universal/poolmate-${version}.zip
2. copy unzipped poolmate-${version} directory to ${poolmate.directory}
3. set executable permissions for ${poolmate.directory}/poolmate-${version}/bin/poolmate

Execute
-------
1. execute ${poolmate-directory}/poolmate-${version}/bin/poolmate

License
-------
>Copyright (c) [2019 - 2023] [Objektwerks]

>Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    * http://www.apache.org/licenses/LICENSE-2.0

>Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
