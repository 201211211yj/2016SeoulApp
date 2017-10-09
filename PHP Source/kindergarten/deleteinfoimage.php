<?php

$con=mysqli_connect("localhost","--------","--------","--------");

if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

mysqli_set_charset($con,"utf8");

$img_source = $_POST['img_source'];

$result = mysqli_query($con, "
DELETE
FROM schoolinfoimage
WHERE img_source = \"$img_source\";
");


mysqli_close($con);

?>
