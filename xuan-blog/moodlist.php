<?php require_once 'files/site_set.php'; ?>
<?php require_once 'utils/an_utils.php'; ?>
<?php require_once 'files/data_common.php'; ?>
<?php require_once 'files/data_post.php'; ?>
<?php 
$this_page = 'moodlist.php';
$this_page_post_type = $constant_post_type_kyrj;

$current_page = 1;
if(isset($_GET['page'])){
    $current_page = $_GET['page'];
}

$tag = 'all';

//以下分页用
$go_where = 'moodlist.php';
$total_count = mc_count_posts_by_tag($constant_post_type_kyrj);
$total_page = ceil($total_count / $mc_post_per_page);

if($total_page == 0){
    $total_page = 1;
}

if($current_page < 1){
    $current_page = 1;
}else if($current_page > $total_page){
    $current_page = $total_page;
}

//显示数据
$mc_posts_kyrj = mc_posts_by_page_tag($constant_post_type_kyrj);          //文章列表
?>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title><?php an_print($site_set['title']); ?></title>
<meta name="keywords" content="<?php an_print($site_set['keywords']); ?>" />
<meta name="description" content="<?php an_print($site_set['description']); ?>" />
<link href="css/base.css" rel="stylesheet">
<link href="css/mood.css" rel="stylesheet">
<!--[if lt IE 9]>
<script src="js/modernizr.js"></script>
<![endif]-->
</head>
<body>
<?php include 'm_header.php'; ?>
<div class="moodlist">
    <h1 class="t_nav">
		<span><?php an_print($site_set['tip3']); ?></span>
		<a href="index.php" class="n1">回到首页</a>
		<a href="moodlist.php" class="n2">开源日记</a>
	</h1>
	
	<div class="bloglist" onclick="">
	    <?php
			reset($mc_posts_kyrj);
			foreach($mc_posts_kyrj as $key=>$post){
		?>
		<ul class="arrow_box">
			<div class="sy" onclick="gotoDetail('<?php an_print('detail.php?id='.$post['id'].'&type='.$this_page_post_type); ?>')">
				<img src="<?php an_print(mc_get_post_url($post['pic_url'])); ?>">
				<p><?php an_print($post['summary']); ?></p>
			</div>
			<span class="dateview"><?php an_print($post['date']); ?></span>
		</ul>
		<?php } ?>
	</div>
	<div class="page">
		<a title="总数"><b><?php an_print($total_count); ?></b></a>
		<?php mc_goto_page(); ?>
	</div>
</div>
<?php include 'm_footer.php'; ?>
<script src="js/silder.js"></script>
<script>
function gotoDetail(url){
	window.location.href = url;
}
</script>

</body>
</html>