bioinformatika-projekt
======================

Run instructions
---------------------

The program has 2 required and 3 optional arguments.

###**Required:**###
**first argument** -  path to file containing overlap information

**second argument** - path to file containing reads infomration

###**Optional:**###

**Unitig layout file path** - you can specify a path to a file in witch the unitig layout will be written. If the file exists, it will be overwritten, if not, it will be created. If this argument is not provided, the unitig layout will be written to the standard output.
<br />Use **-oLayout=filepath** to specify this argument

**Unitig layout overlaps file path** - you can specify a path to a file in witch the ovelap information from the unitig layout will be written. The same writing rules as in the unitig layout file path argument apply. 
<br />Use **-oOverlaps=filepath** to specify this argument

**ε** - real number value in [0,1] interval used in the transitive edge removal. defaults to 0.1
<br />Use **-epsilon=value** to specify this argument

**α** - positive integer value constant used in the transitive edge removal, defaults to 3
<br />Use **-alpha=value** to specify this argument

For additional reference about **ε** and **α** see [this paper](http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.37.9658&rep=rep1&type=pdf) (pg. 13).
<br />All optional argument names are case insensitive.

-----------------------------------
Use **java -jar unitig.jar** to run.

You can find example overlaps and reads files in the testData folder.

**Valid example:**
<br />java -jar unitig.jar testData/overlaps.afg testData/reads.2k.10x.fasta -oLayout=outputLayout.afg -oOverlaps=outputOverlaps.afg -epsilon=0.1 -alpha=3

-----------------------------------
[#24hBioInfo](https://www.facebook.com/24hprojectchallenge "Check out the construction of this awesome project!")

