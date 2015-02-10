<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<head>
<meta charset="utf-8">
<title>ECharts</title>
<script src="/resources/js/jquery.js"></script>
<link href="/resources/css/base.css" rel="stylesheet">
</head>
<body>
	<div class="zu-top" role="navigation">
		<div id="zh-top-inner" class="zg-wrap">
			<a id="zh-top-link-logo" class="zu-top-link-logo" href="/">观点</a>

			<div class="top-nav-profile">
				<a class="zu-top-nav-userinfo " href="/people/song-qing-shu-69"
					id=":0" role="button" aria-haspopup="true" aria-activedescendant="">
					<span class="name">宋青书</span> <img
					src="http://pic1.zhimg.com/93149cb79_s.jpg" class="avatar"> <span
					data-count="0" style="visibility: hidden"
					class="zg-noti-number zu-top-nav-pm-count" id="zh-top-nav-new-pm">

				</span>
				</a>
				<ul id="top-nav-profile-dropdown" class="top-nav-dropdown"
					aria-labelledby=":0">
					<li><a href="/people/song-qing-shu-69" tabindex="-1" id=":1">
							<i class="zg-icon zg-icon-dd-home"></i>我的主页
					</a></li>

					<li><a href="/inbox" tabindex="-1" id=":2"> <i
							class="zg-icon zg-icon-dd-pm"></i>私信 <span data-count="0"
							style="visibility: hidden"
							class="zu-top-nav-pm-count zg-noti-number"
							id="zh-top-nav-pm-count"> </span>
					</a></li>
					<li><a href="/settings" tabindex="-1" id=":3"> <i
							class="zg-icon zg-icon-dd-settings"></i>设置
					</a></li>
					<li><a href="/logout" tabindex="-1" id=":4"> <i
							class="zg-icon zg-icon-dd-logout"></i>退出
					</a></li>
				</ul>

			</div>


			<div class="zu-top-search" id="zh-top-search" role="search">
				<form class="zu-top-search-form form-with-magnify"
					id="zh-top-search-form" action="/search" method="GET">

					<input type="text" placeholder="搜索话题、问题或人..." value=""
						autocomplete="off" name="q" id="q" class="zu-top-search-input"
						aria-haspopup="true"> <input type="hidden"
						value="question" name="type"> <label class="hide-text"
						for="q">知乎搜索</label>
					<button class="magnify-button" type="submit">
						<i class="icon icon-magnify-q"></i><span class="hide-text">搜索</span>
					</button>
				</form>
				<button id="zu-top-add-question" class="zu-top-add-question">提问</button>
			</div>



			<div class="zu-top-nav" id="zg-top-nav">
				<ul class="zu-top-nav-ul zg-clear">

					<li id="zh-top-home-link" class="zu-top-nav-li current"><a
						id="zh-top-link-home" href="/" class="zu-top-nav-link">首页</a></li>
					<li id="zh-top-nav-item-topic"
						class="top-nav-topic-selector zu-top-nav-li "><a
						id="top-nav-dd-topic" href="/topic" class="zu-top-nav-link">话题</a>

					</li>
					<li class="zu-top-nav-li "><a href="/explore"
						class="zu-top-nav-link">发现</a></li>

					<li class="top-nav-noti zu-top-nav-li "><a role="button"
						id="zh-top-nav-count-wrap" href="javascript:;"
						class="zu-top-nav-link"><span class="mobi-arrow"></span>消息<span
							id="zh-top-nav-count" class="zu-top-nav-count zg-noti-number"
							style="display: none;">0</span></a></li>

				</ul>
				<div tabindex="0" role="popup" id="zh-top-nav-live-new"
					class="zu-top-nav-live zu-noti7-popup zg-r5px no-hovercard">
					<div class="zu-top-nav-live-inner zg-r5px">
						<div class="zu-top-live-icon">&nbsp;</div>
						<div id="zh-top-nav-live-new-inner" class="zu-home-noti-inner">
							<div tabindex="0" class="zm-noti7-popup-tab-container clearfix"
								role="tablist">
								<button class="zm-noti7-popup-tab-item message" role="tab">
									<span class="icon">消息</span> <span style="display: none;"
										class="new-noti">0</span>
								</button>
								<button class="zm-noti7-popup-tab-item user" role="tab">
									<span class="icon">用户</span> <span style="display: none;"
										class="new-noti">0</span>
								</button>
								<button class="zm-noti7-popup-tab-item thanks" role="tab">
									<span class="icon">赞同和感谢</span> <span style="display: none;"
										class="new-noti">0</span>
								</button>
							</div>
						</div>
						<div class="zm-noti7-frame-border top"></div>
						<div class="zm-noti7-frame">
							<div class="zm-noti7-content zh-scroller"
								style="position: relative; overflow: hidden;">
								<div class="zh-scroller-inner"
									style="height: 100%; width: 150%; overflow: auto;">
									<div class="zh-scroller-content"
										style="position: static; display: block; visibility: visible; overflow: hidden; width: 315px; min-height: 100%;">
										<div class="zm-noti7-content-inner">
											<div style="display: none;" class="zm-noti7-popup-refresh">
												<img src="/static/img/spinner2.gif" class="noti-spinner">
											</div>
											<div class="zm-noti7-content-body">
												<div class="zm-noti7-popup-loading">
													<img src="/static/img/noti-loading.gif"
														class="noti-spinner">
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="zh-scroller-bar-container"
									style="position: absolute; right: 1px; top: 0px; height: 98px; width: 6px; background: none repeat scroll 0% 0% rgb(102, 102, 102); border: 1px solid rgb(68, 68, 68); opacity: 0; cursor: default; border-radius: 2px; -moz-user-select: none;">
									<div style="-moz-user-select: none;"></div>
								</div>
								<div class="zh-scroller-bar"
									style="position: absolute; right: 2px; top: 2px; opacity: 0.5; background: none repeat scroll 0% 0% rgb(0, 0, 0); width: 6px; border-radius: 3px; cursor: default; -moz-user-select: none; display: none;"></div>
							</div>
							<div style="display: none; position: relative; overflow: hidden;"
								class="zm-noti7-content zh-scroller">
								<div class="zh-scroller-inner"
									style="height: 100%; width: 150%; overflow: auto;">
									<div class="zh-scroller-content"
										style="position: static; display: block; visibility: visible; overflow: hidden; width: 315px; min-height: 100%;">
										<div class="zm-noti7-content-inner">
											<div style="display: none;" class="zm-noti7-popup-refresh">
												<img src="/static/img/spinner2.gif" class="noti-spinner">
											</div>
											<div class="zm-noti7-content-body">
												<div class="zm-noti7-popup-loading">
													<img src="/static/img/noti-loading.gif"
														class="noti-spinner">
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="zh-scroller-bar-container"
									style="position: absolute; right: 1px; top: 0px; height: 98px; width: 6px; background: none repeat scroll 0% 0% rgb(102, 102, 102); border: 1px solid rgb(68, 68, 68); opacity: 0; cursor: default; border-radius: 2px; -moz-user-select: none;">
									<div style="-moz-user-select: none;"></div>
								</div>
								<div class="zh-scroller-bar"
									style="position: absolute; right: 2px; top: 2px; opacity: 0.5; background: none repeat scroll 0% 0% rgb(0, 0, 0); width: 6px; border-radius: 3px; cursor: default; -moz-user-select: none; display: none;"></div>
							</div>
							<div style="display: none; position: relative; overflow: hidden;"
								class="zm-noti7-content zh-scroller">
								<div class="zh-scroller-inner"
									style="height: 100%; width: 150%; overflow: auto;">
									<div class="zh-scroller-content"
										style="position: static; display: block; visibility: visible; overflow: hidden; width: 315px; min-height: 100%;">
										<div class="zm-noti7-content-inner">
											<div style="display: none;" class="zm-noti7-popup-refresh">
												<img src="/static/img/spinner2.gif" class="noti-spinner">
											</div>
											<div class="zm-noti7-content-body">
												<div class="zm-noti7-popup-loading">
													<img src="/static/img/noti-loading.gif"
														class="noti-spinner">
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="zh-scroller-bar-container"
									style="position: absolute; right: 1px; top: 0px; height: 98px; width: 6px; background: none repeat scroll 0% 0% rgb(102, 102, 102); border: 1px solid rgb(68, 68, 68); opacity: 0; cursor: default; border-radius: 2px; -moz-user-select: none;">
									<div style="-moz-user-select: none;"></div>
								</div>
								<div class="zh-scroller-bar"
									style="position: absolute; right: 2px; top: 2px; opacity: 0.5; background: none repeat scroll 0% 0% rgb(0, 0, 0); width: 6px; border-radius: 3px; cursor: default; -moz-user-select: none; display: none;"></div>
							</div>
						</div>
						<div class="zm-noti7-frame-border bottom"></div>
						<div class="zm-noti7-popup-footer">
							<a class="zm-noti7-popup-footer-all zg-right"
								href="/notifications">查看全部 »</a> <a title="通知设置"
								class="zm-noti7-popup-footer-set" href="/settings/notification"><i
								class="zg-icon zg-icon-settings"></i></a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>