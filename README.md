PoolMate
--------
>Open source pool management app.

License
-------
GPL.V3 ( See ./GPL.V3 )

Object Model
------------
* Location(id, street, city, state, zip)
* Owner(id, locationId, poolId, first, last, email)
* Pool (id, currentOwnerId, locationId, gallons, surface, pump, timer, heater)
* Cleaning(id, poolId, on, deck, brush, vacuum, net, skimmerBasket, pumpBasket, pumpFilter)
* Measurement(id, poolId, on, temp, totalHardness, totalChlorine, totalBromine, freeChlorine, pH, totalAlkalinity, cyanuricAcid)
* Additive(id, poolId, on, chemical, unit, amount)
* Repair(id, poolId, cost, description)
* Timer(id, poolId, on, off)

Relational Model
----------------
* Pool 1 ---> 1 Location
* Pool 1 <---> * Owner 1 ---> 1 Location
* Pool 1 ---> * Cleaning | Measurement | Additive | Repair | Timer

Measurements
------------
1. total hardness 0 - 1000      ok = 250 - 500
2. total chlorine 0 - 10        ok = 1 - 5
3. total bromine 0 - 20         ok = 2 - 10
4. free chlorine 0 - 10         ok = 1 - 5
5. ph 6.2 - 8.4                 ok = 7.2 - 7.8
6. total alkalinity 0 - 240     ok = 80 - 120
7. cyanuric acid 0 - 300        ok = 30 - 100

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