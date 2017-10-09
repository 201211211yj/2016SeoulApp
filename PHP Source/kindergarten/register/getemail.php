<?php
$con=mysqli_connect("localhost","--------","--------","--------");

if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

$email = $_POST['email'];

mysqli_set_charset($con,"utf8");


$res = mysqli_query($con, "SELECT email
FROM user
WHERE email = '$email'
LIMIT 0,30");

$result = array();

while($row = mysqli_fetch_array($res)){
  array_push($result,
    array('email'=>$row[0]));
}

echo json_encode(array("result"=>$result));

mysqli_close($con);

?>
