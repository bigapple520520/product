<?php
require_once 'data_album.php';

$mc_album_per_page = 16;

//获取前几个相册
function mc_new_albums_by_num($num){
	global $mc_albums;
	
    $ret_albums = array();
	
	reset($mc_albums);
	$i = 0;
	foreach($mc_albums as $key => $value){
		if($i < $num){
			$ret_albums[$i++] = $value;
		}else{
			return $ret_albums;
		}
	}
	
	return $ret_albums;
}

//返回相册数
function mc_albums_count(){
	global $mc_albums;
	return count($mc_albums);
}

//分页获取数据
function mc_albums_by_page_tag(){
    global $mc_albums, $mc_album_per_page, $current_page, $tag;
	$mc_albums_type = array();
	reset($mc_albums);
	
	//遍历找出指定类型文章
	$i = 0;
	while($key = key($mc_albums)) {
	    $fromI = ($current_page - 1) * $mc_album_per_page;
	    $endI = $current_page * $mc_album_per_page;
	    if($i == $endI){
		    break;
		}
	
		$album= $mc_albums[$key];
		$tags = $album['tags'];
		if($tag =='all' || ($tag !='all' && in_array($tag,$tags))){
			if($i >= $fromI){
			    $mc_albums_type[$i] = $album;
			}
			$i += 1;
		}
		
		next($mc_albums);
	}
	
	return $mc_albums_type;
}

//获取所有的tag
function mc_albums_tags(){
    global $mc_albums;
	$ret_tags = array();
	
	$i = 0;
	reset($mc_albums);
	foreach($mc_albums as $key=>$album){
	    $tags = $album['tags'];
		reset($tags);
		foreach($tags as $key=>$tag){
			if(!in_array($tag,$ret_tags)){
				$ret_tags[$i++] = $tag;
			}
		}
	}
	
	return $ret_tags;
}

//-------------------------------------------以下是分页
function mc_album_goto_page(){
	global $current_page;

	mc_album_goto_left();
	echo '<b>'.$current_page.'</b>';
	mc_album_goto_add1();
	mc_album_goto_right();
}

function mc_album_goto_left() {
	global $current_page, $go_where;

    if($current_page > 1){
	    echo '<a href="'.$go_where.'?page='.($current_page - 1).'" class="nextpage">&lt;</a>';
	}
}

function mc_album_goto_add1() {
	global $total_page, $current_page, $go_where;

    if($current_page < $total_page){
	    echo '<a href="'.$go_where.'?page='.($current_page + 1).'" class="nextpage">'.($current_page + 1).'</a>';
	}
}

function mc_album_goto_right() {
	global $mc_album_per_page, $total_page, $current_page, $total_count, $go_where;

    if($current_page < $total_page - 1){
	    echo '<a href="'.$go_where.'?page='.($current_page + 2).'" class="nextpage">&gt;</a>';
	}
}

?>