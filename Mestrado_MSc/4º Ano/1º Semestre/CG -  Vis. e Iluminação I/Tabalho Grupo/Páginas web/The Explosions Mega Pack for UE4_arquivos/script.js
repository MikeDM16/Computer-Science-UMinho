function detachAllYTVideos (exception) {
    for (var i in ytPlayers) {
        if (i == exception || !ytPlayers[i])
            continue;
        if (typeof ytPlayers[i]['video'] == 'undefined')
            continue;
        if (!!ytPlayers[i].video)
            ytPlayers[i].video.detach ();
    }
}
function clickedVideo (index, id, embedSrc) {
    if (ytPlayers[id].video == null) {
        ytPlayers[id].video = $(document.createElement('iframe'));
        ytPlayers[id].video.attr('src', ytPlayers[id].embed_src + '?autoplay=1&controls=0');
        ytPlayers[id].video.attr('height', '380');
    }
    //detachAllYTVideos(id);
    $('.play-icon').show();
    $('.iframe-sub').show ();
    $(this).siblings('.iframe-sub').hide ();
    $(this).hide();
    ytPlayers[id].video.insertBefore($(this));
    ytPlayers.activeVideo = ytPlayers[id].video;
}
function initYTAPI () {
    return;
    // Tom mod
    ytPlayers = {};
    ytPlayers.activeVideo = null;
    $(".iframe-sub").each (function(index, e) {
        var theID = $(this).attr('embed_code');
        var divID = $(this).attr('id');
        var divIndex = $(this).attr('index');
        var embedSrc = $(this).attr('embed_src');
        ytPlayers[theID] = {
            index: divIndex,
            embed_src: embedSrc,
            video: null
        };
        $(this).siblings(".play-icon").click(function (e) {
            clickedVideo.call (this, divIndex, theID);
        });
    });
    $(".xs_video_link").each (function(e){
        $(this).click(function(e) {
            if (ytPlayers.activeVideo != null) {
                ytPlayers.activeVideo.siblings('.iframe-sub').show ();
                ytPlayers.activeVideo.siblings('.play-icon').show ();
                ytPlayers.activeVideo.detach ();
            }
            var index = $(this).parents('.galleryPager').attr('data-gallery');
            if (index >= 0) {
                galleryLargeSliders [index].stopAuto ();
                gallerySlider [index].stopAuto ();
            }
        });
    });
}
function verifyYTInstall () {
    if (window['YT'] && typeof YT.Player == 'undefined')
        window.setTimeout(verifyYTInstall, 500);
    else
        initYTAPI();
}
verifyYTInstall();