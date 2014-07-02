<?php require_once 'files/site_set.php'; ?>
<?php require_once 'utils/an_utils.php'; ?>
<?php require_once 'files/data_common.php'; ?>
<?php require_once 'files/data_post.php'; ?>
<?php
$post_id = '';
if(!isset($_GET['id'])){
	header("location:index.php");
	exit;
}else{
    $post_id = $_GET['id'];
}

$post_type = 1;
if(!isset($_GET['type'])){
	header("location:index.php");
	exit;
}else{
    $post_type = $_GET['type'];
}

//获取该类型的所有文章
$mc_posts = mc_posts($post_type);

//获取上一篇，本文，下一篇：下标是0,1,2
$ret_posts = mc_get_pre_current_next_from_posts_by_id($mc_posts, $post_id);

$pre_post = $ret_posts[0];
$current_post = $ret_posts[1];
$next_post = $ret_posts[2];
if(count($current_post) == 0){
    //异常情况下文章不存在
	an_print('当前文件不存在！');
    exit;
}

//获取相关文章
$related_posts = mc_get_posts_by_tags($mc_posts, $post_type, 6);
$new_posts = mc_new_posts_by_num($post_type, 8);
$ranking_posts = mc_new_posts_by_num($post_type, 6);
?>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title><?php an_print($site_set['title']); ?></title>
<meta name="keywords" content="<?php an_print($site_set['keywords']); ?>" />
<meta name="description" content="<?php an_print($site_set['description']); ?>" />
<link href="css/base.css" rel="stylesheet">
<link href="css/new.css" rel="stylesheet">
<!--[if lt IE 9]>
<script src="js/modernizr.js"></script>
<![endif]-->
</head>
<body>
<?php include 'm_header.php'; ?>
<article class="blogs">
<h1 class="t_nav">
	<span>您当前的位置：
		<a href="index.php">首页</a>&nbsp;&gt;&nbsp;
		<a href="<?php an_print(mc_type_to_where($post_type)); ?>"><?php an_print(mc_type_to_description($post_type)); ?></a>&nbsp;&gt;&nbsp;
		<a href="#">文章</a>
	</span>
	<a href="index.php" class="n1">返回首页</a>
	<a href="<?php an_print(mc_type_to_where($post_type)); ?>" class="n2"><?php an_print(mc_type_to_description($post_type)); ?></a>
</h1>

<div class="index_about">
	<h2 class="c_titile"><?php an_print($current_post['title']); ?></h2>
    <p class="box_c">
		<span class="d_time">发布时间：<?php an_print($current_post['date']); ?></span>
		<span>作者：<?php an_print($current_post['edit_name']); ?></span>
	</p>
    <ul class="infos">
        <?php an_print(mc_the_content($current_post['id'])); ?>
    </ul>
    <div class="keybq"><p><span>关键字词</span>：<?php echo implode(',',$current_post['tags']); ?></p></div>
	
    <div class="nextinfo">
	    <?php 
			if(count($pre_post) != 0){
			    an_print('<p>上一篇：<a href="detail.php?id='.$pre_post['id'].'&type='.$pre_post['types'][0].'">'.$pre_post['title'].'</a></p>');
			}
		?>
		<?php 
			if(count($next_post) != 0){
			    an_print('<p>下一篇：<a href="detail.php?id='.$next_post['id'].'&type='.$next_post['types'][0].'">'.$next_post['title'].'</a></p>');
			}
		?>
    </div>
	
    <div class="otherlink">
        <h2>相关文章</h2>
        <ul>
		<?php 
			reset($related_posts);
			foreach($related_posts as $key => $post){
				an_print('<li><a href="detail.php?id='.$post['id'].'&type='.$post['types'][0].'" title="'.$post['title'].'">'.$post['title'].'</a></li>');
			}
		?>
        </ul>
    </div>
</div>

<aside class="right">
	<!-- 百度分享 -->
	<div id="bdshare" class="bdshare_t bds_tools_32 get-codes-bdshare"><a class="bds_tsina"></a><a class="bds_qzone"></a><a class="bds_tqq"></a><a class="bds_renren"></a><span class="bds_more"></span><a class="shareCount"></a></div>
	<script type="text/javascript" id="bdshare_js" data="type=tools&amp;uid=6574585" ></script> 
	<script type="text/javascript" id="bdshell_js"></script> 
	<script type="text/javascript">
		document.getElementById("bdshell_js").src = "http://bdimg.share.baidu.com/static/js/shell_v2.js?cdnversion=" + Math.ceil(new Date()/3600000)
	</script>
	<!-- 百度分享 -->
    <div class="blank"></div>
    <div class="news">
        <h3><p>文章<span>最新</span></p></h3>
		<ul class="rank">
		<?php 
			reset($new_posts);
			foreach($new_posts as $key => $post){
				an_print('<li><a href="detail.php?id='.$post['id'].'&type='.$post['types'][0].'" title="'.$post['title'].'" target="_blank">'.$post['title'].'</a></li>');
			}
		?>
		</ul>
		<h3 class="ph"><p>点击<span>排行</span></p></h3>
		<ul class="paih">
		<?php 
			reset($ranking_posts);
			foreach($ranking_posts as $key => $post){
				an_print('<li><a href="detail.php?id='.$post['id'].'&type='.$post['types'][0].'" title="'.$post['title'].'" target="_blank">'.$post['title'].'</a></li>');
			}
		?>
		</ul>
    </div>
    <div class="visitors">
        <h3><p>最近访客</p></h3>
        <ul></ul>
    </div>
</aside>
</article>
<?php an_print(mc_type_to_nav($post_type)); ?>
<?php include 'm_footer.php'; ?>
<script>
document.getElementById('topnav').getElementsByTagName('a')[<?php an_print(mc_type_to_nav($post_type)); ?>].id = 'topnav_current';</script>
</body>
</html>