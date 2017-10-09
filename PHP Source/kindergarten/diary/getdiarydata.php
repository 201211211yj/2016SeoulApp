<?php

$con=mysqli_connect("localhost","--------","--------","--------");

if(mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

$date = $_POST['date'];
$kinder_id = $_POST['kinder_id'];


mysqli_set_charset($con,"utf8");


$res = mysqli_query($con, "SELECT diary 
FROM diarytable
WHERE date = \"$date\" AND kinder_id = \"$kinder_id\""
);

$result = array();

while($row = mysqli_fetch_array($res)){
  array_push($result,
    array('diary'=>$row[0]));
}

echo json_encode(array("result"=>$result));

mysqli_close($con);

?>

