<?php

$con=mysqli_connect("localhost","--------","--------","--------");
  
if (mysqli_connect_errno($con))  
{  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

mysqli_set_charset($con,"utf8");

$name = $_POST['name'];

$res = mysqli_query($con, "SELECT id,name, phone, kindertype, address, childlimit, cctv, bus
	FROM kindergarten
	WHERE name LIKE '%$name%'
	ORDER BY name
	LIMIT 0,100");

$result = array();

while($row = mysqli_fetch_array($res)){
  array_push($result,
    array('id'=>$row[0],'name'=>$row[1],'phone'=>$row[2],'kindertype'=>$row[3],'address'=>$row[4],'childlimit'=>$row[5],'cctv'=>$row[6],'bus'=>$row[7]
    ));
}

echo json_encode(array("result"=>$result));

mysqli_close($con);

?>
