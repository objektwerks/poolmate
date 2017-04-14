PoolMate
--------
>Open source pool management app.

License
-------
GPL.V3 ( See ./GPL.V3 )

Object Model
------------
* Pool (id, gallons, owner)
* Measurement (id, poolid, created, deck, brush, net, basket, filter, tablet, chlorine, pH, alkalinity, temp)

Relational Model
----------------
* Pool 1 ---> * Measurement

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