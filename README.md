PoolMate
--------
>Open source pool management app.

License
-------
GPL.V3 ( See ./GPL.V3 )

Object Model
------------
* Owner(id, name, email, street, city, state, zip)
* Pool (id, ownerid, gallons)
* Cleaning(id, poolid, on, deck, brush, net, basket, filter)
* Measurement(id, poolid, on, tablet, ci, ph, alky, temp)
* Chemical(id, name, unit)
* Additive(id, poolid, chemicalid, on, amount)
* Repair(id, poolid, cost, description)

Relational Model
----------------
* Owner 1 ---> * Pool 1 ---> * Cleaning | Measurement | Repair | Additive 1 ---> 1 Chemical

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