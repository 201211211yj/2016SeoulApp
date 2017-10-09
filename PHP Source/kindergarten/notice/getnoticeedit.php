<?php
$con=mysqli_connect("localhost","--------","--------","--------");

if(mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

$notice_id = $_POST['notice_id'];

mysqli_set_charset($con,"utf8");


$res = mysqli_query($con, "SELECT notice_title,notice_text,kinder_id
	FROM noticetable
	WHERE notice_id = \"$notice_id\""
	);

$result = array();

while($row = mysqli_fetch_array($res)){
  	array_push($result,
   	 	array('notice_title'=>$row[0],'notice_text'=>$row[1],'kinder_id'=>$row[2]));
}

echo json_encode(array("result"=>$result));

mysqli_close($con);

?>
