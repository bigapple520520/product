<?php require_once 'posts/index/publish.php'; ?>
<?php
$mc_post_per_page = 6;//每页数据条数

//$mc_page_num：当前页
//$mc_post_count：一共有的数据

//随机获取一张图片
function mc_post_image_url(){
    global $post_pic_num;
	
	$i = rand(1, $post_pic_num);
	return 'files/posts-image/'.$i.'.jpg';
}

//取到文章的图片
function mc_get_post_url($url){
	global $constant_can_use_postimage_num;
    if($url == ''){
		$i = rand(1, $constant_can_use_postimage_num);
		return 'files/posts-image/'.$i.'.jpg';
	}else{
		return $url;
	}
}

//获取指定随机数
function mc_rand($from, $end){
    return rand($from, $end);
}

//类型转成类型的描述
function mc_types_to_descriptions($types){
	global $constant_post_type_tjwz, $constant_post_type_ddsy, $constant_post_type_kyrj, $constant_post_type_jszt; 
	
	$ret_descriptions = array();
	
	if(isset($types)){
		reset($types);
	}else{
		return $ret_descriptions;
	}
	
	$i = 0;
	foreach($types as $key=>$type){
		if($type == $constant_post_type_tjwz){
			$ret_descriptions[$i++] = '推荐文章';
		}else if($type == $constant_post_type_ddsy){
			$ret_descriptions[$i++] = '点点碎语';
		}else if($type == $constant_post_type_kyrj){
			$ret_descriptions[$i++] = '开源日记';
		}else if($type == $constant_post_type_jszt){
			$ret_descriptions[$i++] = '技术杂谈';
		}
	}
	
	if(count($ret_descriptions) == 0){
		$ret_descriptions[0] = '推荐文章';
	}
	return $ret_descriptions;
}

//获取所有的tag
function mc_tags($type){
    global $mc_posts;
	$ret_tags = array();
	
	$i = 0;
	reset($mc_posts);
	foreach($mc_posts as $key=>$post){
	    $tags = $post['tags'];
		$types = $post['types'];
		if(in_array($type,$types)){
			reset($tags);
			foreach($tags as $key=>$tag){
				if(!in_array($tag,$ret_tags)){
					$ret_tags[$i++] = $tag;
				}
			}
		}
	}
	
	return $ret_tags;
}

//获取最新前num条数据
function mc_new_posts_by_num($type, $num){
	global $mc_posts;
	$ret = array();
	
	reset($mc_posts);
	$i = 0;
	foreach($mc_posts as $key=>$post){
		$post = $mc_posts[$key];
	    $types = $post['types'];
		if($type == 'all' || in_array($type,$types)){
		    if($i >= $num){
			    return $ret;
			}else{
			    $ret[$i] = $post;
				$i += 1;
			}
		}
	}
	
	return $ret;
}

//取类型下的所有文章
function mc_posts($type){
	global $mc_posts;
	$ret = array();
	
	reset($mc_posts);
	$i = 0;
	foreach($mc_posts as $key=>$post){
		$types = $post['types'];
		if(in_array($type, $types)){
			$ret[$i++] = $post;
		}
	}
	
	return $ret;
}

//分页获取数据
function mc_posts_by_page_tag($type){
    global $mc_posts, $mc_post_per_page, $current_page, $tag;
	$mc_posts_type = array();
	reset($mc_posts);
	
	//遍历找出指定类型文章
	$i = 0;
	while($key = key($mc_posts)) {
	    $fromI = ($current_page - 1) * $mc_post_per_page;
	    $endI = $current_page * $mc_post_per_page;
	    if($i == $endI){
		    break;
		}
	
		$post= $mc_posts[$key];
	    $types = $post['types'];
		$tags = $post['tags'];
		if(in_array($type,$types) && ($tag =='all' || ($tag !='all' && in_array($tag,$tags)))){
			if($i >= $fromI){
			    $mc_posts_type[$i] = $post;
			}
			$i += 1;
		}
		
		next($mc_posts);
	}
	
	return $mc_posts_type;
}

//统计总共有几条
function mc_count_posts_by_tag($type){
    global $mc_posts, $tag;
	reset($mc_posts);
	
	$i = 0;
	while($key = key($mc_posts)) {
		$post= $mc_posts[$key];
	    $types = $post['types'];
		$tags = $post['tags'];
		if(in_array($type,$types) && ($tag == 'all' || ($tag != 'all' && in_array($tag,$tags)))){
			$i += 1;
		}
		
		next($mc_posts);
	}
	
	return $i;
}

//根据id获取内容
function mc_the_content($post_id) {
	$data = unserialize(file_get_contents(realpath('.').'/files/posts/data/'.$post_id.'.dat')); 
	return $data['content'];
}

function mc_has_left($current_page, $post_count){
    return $current_page > 1;
}

function mc_has_right($current_page, $post_count){
    $total_page = ($post_count / $mc_post_per_page) + 1;
    return $current_page + 1 < $total_page;
}

function mc_goto_page(){
	global $current_page;

	mc_goto_left();
	echo '<b>'.$current_page.'</b>';
	mc_goto_add1();
	mc_goto_right();
}

function mc_goto_left() {
	global $current_page, $go_where;

    if($current_page > 1){
	    echo '<a href="'.$go_where.'?page='.($current_page - 1).'" class="nextpage">&lt;</a>';
	}
}

function mc_goto_add1() {
	global $total_page, $current_page, $go_where;

    if($current_page < $total_page){
	    echo '<a href="'.$go_where.'?page='.($current_page + 1).'" class="nextpage">'.($current_page + 1).'</a>';
	}
}

function mc_goto_right() {
	global $mc_post_per_page, $total_page, $current_page, $post_count, $go_where;

    if($current_page < $total_page - 1){
	    echo '<a href="'.$go_where.'?page='.($current_page + 2).'" class="nextpage">&gt;</a>';
	}
}

//---------------------------------------------detail.php---------------------------------------------------

//获取类型的描述
function mc_type_to_description($type){
    global $constant_post_type_tjwz, $constant_post_type_ddsy, $constant_post_type_kyrj, $constant_post_type_jszt;
	
	if($type == $constant_post_type_tjwz){
	    return '推荐文章';
	}else if($type == $constant_post_type_ddsy){
	    return '点点碎语';
	}else if($type == $constant_post_type_kyrj){
	    return '开源日志';
	}else if($type == $constant_post_type_jszt){
	    return '技术杂谈';
	}
}

//根据类型取到跳转页面
function mc_type_to_where($type){
	global $constant_post_type_tjwz, $constant_post_type_ddsy, $constant_post_type_kyrj, $constant_post_type_jszt;
	
	if($type == $constant_post_type_tjwz){
	    return 'index.php';
	}else if($type == $constant_post_type_ddsy){
	    return 'newlist.php';
	}else if($type == $constant_post_type_kyrj){
	    return 'moodlist.php';
	}else if($type == $constant_post_type_jszt){
	    return 'knowledge.php';
	}
}

//指定类型获取导航
function mc_type_to_nav($type){
	global $constant_post_type_tjwz, $constant_post_type_ddsy, $constant_post_type_kyrj, $constant_post_type_jszt;
	
	if($type == $constant_post_type_tjwz){
	    return '0';
	}else if($type == $constant_post_type_ddsy){
	    return '2';
	}else if($type == $constant_post_type_kyrj){
	    return '3';
	}else if($type == $constant_post_type_jszt){
	    return '5';
	}
}

//从数组中挑选出对应id的前一篇，后一篇和其本身的post信息
function mc_get_pre_current_next_from_posts_by_id($posts, $id){
	$ret_posts[0] = array();
	$ret_posts[1] = array();
	$ret_posts[2] = array();
	
	$count = count($posts);
	
	reset($posts);
	$i = 0;
	foreach($posts as $key => $value){
		if($value['id'] == $id){
			$ret_posts[1] = $value;
			if($count > 1){
				if($i == 0){
					$ret_posts[2] = $posts[$i+1];
				}else if($i == ($count-1)){
					$ret_posts[0] = $posts[$i-1];
				}else{
					$ret_posts[0] = $posts[$i-1];
					$ret_posts[2] = $posts[$i+1];
				}
			}
		}
		
		$i++;
	}
	
	return $ret_posts;
}

//根据tags
function mc_get_posts_by_tags($posts, $tag, $num){
	$ret_posts = array();

	reset($posts);
	$i = 0;
    foreach($posts as $key => $value){
		$temp_tags = $value['tags'];
		reset($temp_tags);
		if(in_array($tag, $temp_tags)){
			$ret_posts[$i++] = $value;
			break;
		}
		
		if($i == $num){
			return $ret_posts;
		}
	}
	
	return $ret_posts;
}

?>
