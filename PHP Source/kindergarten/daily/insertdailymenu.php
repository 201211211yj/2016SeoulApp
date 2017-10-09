
<?php
$con=mysqli_connect("localhost","--------","--------","--------");

mysqli_set_charset($con,"utf8");


if(mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

$kinder_id = $_POST['kinder_id'];
$date = $_POST['date'];

$menu1	= $_POST['menu1'];
$menu2	= $_POST['menu2'];
$menu3	= $_POST['menu3'];
$menu4	= $_POST['menu4'];
$menu5	= $_POST['menu5'];
$menu6	= $_POST['menu6'];
$menu7	= $_POST['menu7'];
$menu8	= $_POST['menu8'];
$menu9	= $_POST['menu9'];
$menu10	= $_POST['menu10'];

$from1	=	$_POST['from1'];
$from2	=	$_POST['from2'];
$from3	=	$_POST['from3'];
$from4	=	$_POST['from4'];
$from5	=	$_POST['from5'];
$from6	=	$_POST['from6'];
$from7	=	$_POST['from7'];
$from8	=	$_POST['from8'];
$from9	=	$_POST['from9'];
$from10	=	$_POST['from10'];

$res = mysqli_query($con, "
SELECT menu1 FROM dailymenutable WHERE kinder_id = '111' AND date = '2016-10-29';
");

$result = array();

while($row = mysqli_fetch_array($res)){
  array_push($result,
    array('menu1'=>$row[0]));
}

echo json_encode(array("result"=>$result));

if($result){
        $result = mysqli_query($con, "
        UPDATE dailymenutable 
	SET 
	menu1=\"$menu1\",from1=\"$from1\",
	menu2=\"$menu2\",from2=\"$from2\",
	menu3=\"$menu3\",from3=\"$from3\",
	menu4=\"$menu4\",from4=\"$from4\",
	menu5=\"$menu5\",from5=\"$from5\",
	menu6=\"$menu6\",from6=\"$from6\",
	menu7=\"$menu7\",from7=\"$from7\",
	menu8=\"$menu8\",from8=\"$from8\",
	menu9=\"$menu9\",from9=\"$from9\",
	menu10=\"$menu10\",from10=\"$from10\"  
	
	WHERE kinder_id = '$kinder_id' AND date = '$date';
        ");
  }
  else{
    $result = mysqli_query($con, "
        INSERT INTO dailymenutable 
	VALUES('$kinder_id','$date',
		\"$menu1\",\"$from1\",
		\"$menu2\",\"$from2\",
		\"$menu3\",\"$from3\",
		\"$menu4\",\"$from4\",
		\"$menu5\",\"$from5\",
		\"$menu6\",\"$from6\",
		\"$menu7\",\"$from7\",
		\"$menu8\",\"$from8\",
		\"$menu9\",\"$from9\",
		\"$menu10\",\"$from10\");
        ");
  }

mysqli_close($con);

?>
