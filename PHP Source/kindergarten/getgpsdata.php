<?php  
  
$con=mysqli_connect("localhost","--------","--------","--------");  
  
if (mysqli_connect_errno($con))  
{  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();  
}  
   
$mylat = $_POST['mylat'];  
$mylng = $_POST['mylng'];  
 
 
mysqli_set_charset($con,"utf8");  
  
   
$res = mysqli_query($con, "SELECT id,name,kindertype,address,latitude,longitude,(6371*acos(cos(radians($mylat))*cos(radians(latitude))*cos(radians(longitude)
-radians($mylng))+sin(radians($mylat))*sin(radians(latitude))))AS distance
FROM kindergarten
ORDER BY distance 
LIMIT 0,15");

$result = array();  
   
while($row = mysqli_fetch_array($res)){  
  array_push($result,  
    array('id'=>$row[0],'name'=>$row[1],'type'=>$row[2],'address'=>$row[3],'latitude'=>$row[4],'longitude'=>$row[5]  
    ));  
}  
   
echo json_encode(array("result"=>$result));  
   
mysqli_close($con);  
   
?>   
