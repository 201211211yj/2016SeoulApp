<?php
$con=mysqli_connect("localhost","--------","--------","--------");
if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

$kinder_id = $_POST['kinder_id'];

mysqli_set_charset($con,"utf8");

$res = mysqli_query($con, "SELECT email,child_name
FROM user
WHERE kinder_id = '$kinder_id' AND ready_register = '1'
LIMIT 0,1000");

$result = array();

while($row = mysqli_fetch_array($res)){
  array_push($result,
    array('email'=>$row[0],'child_name'=>$row[1]));
}

echo json_encode(array("result"=>$result));

mysqli_close($con);

?>
