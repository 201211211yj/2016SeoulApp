<?php

$con=mysqli_connect("localhost","--------","--------","--------");
  
if (mysqli_connect_errno($con))  
{  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

mysqli_set_charset($con,"utf8");

$loc = $_POST['loc'];
$type = $_POST['type'];
$bus = $_POST['bus'];
$cctv = $_POST['cctv'];

$bustrue = $bus;
$cctvtrue = $cctv;

if($bustrue && $cctvtrue){
	$res = mysqli_query($con, "SELECT id,name, phone, kindertype, address, childlimit, cctv, bus
	FROM kindergarten
	WHERE gu =  \"$loc\" AND
	kindertype = \"$type\" AND
	bus = \"Y\" AND
	cctv > 0
	ORDER BY name
	LIMIT 0,100");
}else if($bustrue && !$cctvtrue){
	$res = mysqli_query($con, "SELECT id,name, phone, kindertype, address, childlimit, cctv, bus
	FROM kindergarten
	WHERE gu =  \"$loc\" AND
	kindertype = \"$type\" AND
	bus = \"Y\" AND
	cctv = 0
	ORDER BY name
	LIMIT 0,100");
}else if(!$bustrue && $cctvtrue){
	$res = mysqli_query($con, "SELECT id,name, phone, kindertype, address, childlimit, cctv, bus
	FROM kindergarten
	WHERE gu =  \"$loc\" AND
	kindertype = \"$type\" AND
	bus = \"N\" AND
	cctv > 0
	ORDER BY name
	LIMIT 0,100");
}else{
	$res = mysqli_query($con, "SELECT id,name, phone, kindertype, address, childlimit, cctv, bus
	FROM kindergarten
	WHERE gu =  \"$loc\" AND
	kindertype = \"$type\" AND
	bus = \"N\" AND
	cctv = 0
	ORDER BY name
	LIMIT 0,100");
}

$result = array();

while($row = mysqli_fetch_array($res)){
  array_push($result,
    array('id'=>$row[0],'name'=>$row[1],'phone'=>$row[2],'kindertype'=>$row[3],'address'=>$row[4],'childlimit'=>$row[5],'cctv'=>$row[6],'bus'=>$row[7]
    ));
}

echo json_encode(array("result"=>$result));

mysqli_close($con);

?>
