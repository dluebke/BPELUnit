<?php
  $title = "Documentation";
  require_once("_header.php");
?>

<p>There are various tutorials available for working with BPELUnit. We link those we know that they exist. If you know others, please let us know on the mailing list!</p>

<h2>Basic Concepts</h2>
<ul>
	<li>Tutorial <a href="http://www.se.uni-hannover.de/lehre/tutorials/BPELUnit-HelloBPEL.php">Testing a Hello World Process</a></li>
	<li>Tutorial: <a href="http://www.se.uni-hannover.de/lehre/tutorials/BPELUnit-Coverage1.php">Unit Testing BPEL processes with Test Coverage - Part I</a></li>
	<li>Tutorial: <a href="http://www.se.uni-hannover.de/lehre/tutorials/BPELUnit-Coverage2.php">Unit Testing BPEL processes with Test Coverage - Part II</a></li>
</ul>

<h2>Advanced Testing Concepts</h2>
<ul>
	<!--<li>Mocking in BPELUnit</li> -->
	<!--<li>Testing long-running Processes</li> -->
	<li><a href="http://www.se.uni-hannover.de/forschung/soa/bpelunit/testing-fault-scenarios.php">Testing Fault Scenarios</a> (under construction)</li>
	<li><a href="http://www.se.uni-hannover.de/forschung/soa/bpelunit/bpelunit-ant.php">Automating Tests with Ant and BPELUnit (e.g. for nightly builds)</a></li>
	<!--<li>Test Coverage Calculation</li> -->
	<li><a href="http://www.se.uni-hannover.de/forschung/soa/bpelunit/bpelunit-roch-criteria.php">Comparing BPELUnit With Roch's Criterias</a></li>
</ul>

<h2>Books</h2>
<ul>
	<li><a href="http://www.amazon.de/Gesch%C3%A4ftsprozesse-automatisieren-BPEL-Daniel-L%C3%BCbke/dp/389864670X/ref=sr_1_2?ie=UTF8&s=books&qid=1285516524&sr=8-2">Geschäftsprozesse automatisieren mit BPEL</a> (German only) will use BPELUnit for demonstrating testing BPEL processes</li>
</ul>

<?php
	require_once("_footer.php");
?>