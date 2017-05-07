PoolMate
--------
>Open source pool management app.

License
-------
GPL.V3 ( See ./GPL.V3 )

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

Measurements
------------
1. total hardness 0 - 1000      ok = 250 - 500      ideal = 375
2. total chlorine 0 - 10        ok = 1 - 5          ideal = 3
3. total bromine 0 - 20         ok = 2 - 10         ideal = 5
4. free chlorine 0 - 10         ok = 1 - 5          ideal = 3
5. ph 6.2 - 8.4                 ok = 7.2 - 7.8      ideal = 7.5
6. total alkalinity 0 - 240     ok = 80 - 120       ideal = 100
7. cyanuric acid 0 - 300        ok = 30 - 100       ideal = 50
 
** ounces, gallons, pounds

View
----
east pane - supplies, lifecycles, cleanings, measurements, additives
west pane - pools, owners, surfaces, pumps, timers, heaters, repairs

Charts
------
1. supplies - bar chart ( x = purchased, y = cost, c = item )
2. measurements - line chart ( x = on, y = chemical ** )
3. additives - bar chart ( x = on, y = amount, c = chemical )
4. repairs - line chart ( x = on, y = cost )

** a line chart for each measured chemical, to include temp

Todo
----
1. Additive lookup for chemical and unit
2. Supply lookup for unit
3. Lookup for unit ( ounces, gallons, pounds )

Test
----
1. sbt clean test

Run
---
1. sbt clean test run

Package
-------
1. sbt clean test universal:packageBin

Install
-------
1. copy universal directory to target directory (i.e., home directory ~ )
2. unzip ~/universal/poolmate-${version}.zip
3. set executable permissions for ~/universal/poolmate-${version}/bin/poolmate

Execute
-------
1. execute ~/universal/poolmate-${version}/bin/poolmate