<?php
$con=mysqli_connect("localhost","--------","--------","--------");

if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

mysqli_set_charset($con,"utf8");

$id = $_POST['id'];

$res = mysqli_query($con, "
SELECT img_source
FROM schoolinfoimage
WHERE kinder_id = '$id';
");

$result = array();

while($row = mysqli_fetch_array($res)){
  array_push($result,
    array('img_source'=>$row[0]));
}

echo json_encode(array("result"=>$result));

mysqli_close($con);

?>
