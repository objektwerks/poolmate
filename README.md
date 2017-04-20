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
* Measurement(id, poolId, on, temp, totalHardness, totalChlorine, totalBromine, freeChlorine, pH, totalAlkalinity, cyanuricAcid)
* Additive(id, poolId, on, chemical, unit, amount)
* Repair(id, poolId, on, cost, description)

Relational Model
----------------
* Pool 1 ---> * Owner
* Pool 1 ---> * Surface | Pump | Timer | Heater
* Pool 1 ---> * Cleaning | Measurement | Additive | Repair

Measurements
------------
1. total hardness 0 - 1000      ok = 250 - 500      ideal = 375
2. total chlorine 0 - 10        ok = 1 - 5          ideal = 3
3. total bromine 0 - 20         ok = 2 - 10         ideal = 5
4. free chlorine 0 - 10         ok = 1 - 5          ideal = 3
5. ph 6.2 - 8.4                 ok = 7.2 - 7.8      ideal = 7.5
6. total alkalinity 0 - 240     ok = 80 - 120       ideal = 100
7. cyanuric acid 0 - 300        ok = 30 - 100       ideal = 50

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