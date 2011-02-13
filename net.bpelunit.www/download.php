<?php
  $title = "Download";
  require_once("_header.php");
?>

<h2>Installing BPELUnit in Eclipse</h2>

<p>For users, the simplest way of installing BPELUnit is to use the update site. In the Help menu, choose Software Updates..., where you choose the Available Software tab. Click Add Site... and enter <a href="http://update.bpelunit.net">http://update.bpelunit.net</a> as the new site's location. Click OK.</p>

<p>Once the BPELUnit Update Site is added, simply check BPELUnit Update Site, click Install... and follow the instructions. After Eclipse has restarted, BPELUnit should be installed.</p>

<p>You can verify the installation by opening the plugin information dialog (Help / About Eclipse Platform / Plug-in Details) and look for BPELUnit. You should have three plugins.</p>

<p>For getting started with how to use BPELUnit, please refer to the two tutorials:</p>

<ul>
	<li>Testing a Hello World Process</li>
	<li>Testing a Simple BPEL Process with Mocking</li>
</ul>

<h2>Installing BPELUnit standalone</h2>

<p>If you do not use Eclipse or yout want to also use the integration into Apache Ant, you will need to have the standalone version of BPELUnit. You can extract it to any directory, e.g. C:\Program Files\ under Windows. For starting BPELUnit in Windows, a batch file bpelunit.bat is supplied. For the batch file to work, you have to set BPELUNIT_HOME to the BPELUnit directory and JAVA_HOME to your Java installation directory.</p>

<?php
	require_once("_footer.php");
?>