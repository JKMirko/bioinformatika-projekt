bioinformatika-projekt
======================

Run instructions
---------------------

SYNOPSIS
```shell
$ java -jar unitig.jar path_to_overlaps path_to_reads [-oLayout=outputLayout.afg] \
> [-oOverlaps=outputOverlaps.afg] [-epsilon=0.1] [-alpha=3]
```

The program has 2 required and 4 optional arguments.

###**Required:**

* **path_to_overlaps** -  path to file containing overlap information

* **path_to_reads**    -  path to file containing reads information

###**Optional:**

* **-oLayout**   -  Specifies *unitig layout file path* in which the unitig layout will be written. If the file exists, it will be overwritten, if not, it will be created. If this argument is not provided, the unitig layout will be written to the standard output.
<br />Use **-oLayout=filepath** to specify this argument

* **-oOverlaps** -  Specifies *unitig layout overlaps file path* in which the overlap information from the unitig layout will be written. The same writing rules apply as in the unitig layout file path argument. 
<br />Use **-oOverlaps=filepath** to specify this argument

* **ε**          -  Real number value from [0,1] interval used in the transitive edge removal. Default is 0.1.
<br />Use **-epsilon=value** to specify this argument

* **α**          -  Positive integer value constant used in the transitive edge removal. Default is 3.
<br />Use **-alpha=value** to specify this argument

For additional reference about **ε** and **α** see [this paper](http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.37.9658&rep=rep1&type=pdf) (pg. 13).
<br />All optional argument names are case insensitive.

-----------------------------------
Use **java -jar unitig.jar** to run.

You can find example overlaps and reads files in the testData folder.

**Valid example:**
```shell
$ java -jar unitig.jar testData/overlaps.afg testData/reads.2k.10x.fasta -oLayout=outputLayout.afg \
> -oOverlaps=outputOverlaps.afg -epsilon=0.1 -alpha=3
```

-----------------------------------
[#24hBioInfo](https://www.facebook.com/24hprojectchallenge "Check out the construction of this awesome project!")
