<?php require_once 'files/site_set.php'; ?>
<?php require_once 'utils/an_utils.php'; ?>
<?php require_once 'files/data_common.php'; ?>
<?php require_once 'files/about_me.php'; ?>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title><?php echo $site_set['title']; ?></title>
<meta name="keywords" content="<?php echo $site_set['keywords']; ?>" />
<meta name="description" content="<?php echo $site_set['description']; ?>" />
<link href="css/base.css" rel="stylesheet">
<link href="css/about.css" rel="stylesheet">
<!--[if lt IE 9]>
<script src="js/modernizr.js"></script>
<![endif]-->
<!--
<link href='http://fonts.googleapis.com/css?family=Architects+Daughter' rel='stylesheet' type='text/css'>
-->
</head>
<body>
<?php include 'm_header.php'; ?>
<article class="aboutcon">
	<h1 class="t_nav">
		<span><?php an_print($site_set['tip1']); ?></span>
		<a href="index.php" class="n1">回到首页</a><a href="about.php" class="n2">关于我</a>
	</h1>
	<div class="about left">
	    <h2>Just about me</h2>
	    <ul><?php echo file_get_contents('files/'.$about_me['intro']); ?></ul>
	</div>
	<aside class="right">  
		<div class="about_c">
			<p>姓名：<span><?php echo $about_me['real_name']; ?></span> </p>
			<p>生日：<?php echo $about_me['birthday']; ?></p>
			<p>现居：<?php echo $about_me['live_now']; ?></p>
			<p>邮箱：<?php echo $about_me['mail']; ?></p>
			<p>QQ号：<?php echo $about_me['qq']; ?></p>
			<p>爱好：<?php echo $about_me['hobby']; ?></p>
		</div>     
	</aside>
</article>
<?php include 'm_footer.php'; ?>
<script src="js/silder.js"></script>
</body>
</html>