<?php
$con=mysqli_connect("localhost","--------","--------","--------");

if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

mysqli_set_charset($con,"utf8");

$img_source = $_POST['img_source'];
$id = $_POST['id'];

$result = mysqli_query($con, "
INSERT
INTO schoolinfoimage
VALUES ( '$id', \"$img_source\",NOW() );
");


mysqli_close($con);

?>
