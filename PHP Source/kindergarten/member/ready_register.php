<?php
$con=mysqli_connect("localhost","--------","--------","--------");

if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

$email = $_POST['email'];
$kinder_id = $_POST['kinder_id'];

mysqli_set_charset($con,"utf8");

$res = mysqli_query($con, "SELECT kinder_id, ready_register
FROM user
WHERE email = \"$email\"
LIMIT 0,1000");


//$result = array();

//while($row = mysqli_fetch_array($res))
//{
// array_push($result,
//    array('kinder_id'=>$row[0],'ready_register'=>$row[1]));
//}

//echo json_encode(array("result"=>$result));

$row = mysqli_fetch_array($res);

if($row[0]==null&&strcmp($row[1],"\"0\"")){
	$result = mysqli_query	($con, "UPDATE user
					SET kinder_id = $kinder_id, ready_register = '1'
					WHERE email = \"$email\"
					");
	if($result)
		echo json_encode('success');
	else
		echo json_encode('failure');
}
else
	echo json_encode('cancel');

mysqli_close($con);

?>
