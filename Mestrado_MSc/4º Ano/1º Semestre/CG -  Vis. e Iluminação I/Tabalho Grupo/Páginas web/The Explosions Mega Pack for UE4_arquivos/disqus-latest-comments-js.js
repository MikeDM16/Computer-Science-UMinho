( function ($) {
	'use strict';

	var dlcsettings = disqus_latest_comments_js_settings;

	var disqus_minute_ago = dlcsettings.disqus_minute_ago,
		disqus_minutes_ago = dlcsettings.disqus_minutes_ago,
		disqus_hour_ago = dlcsettings.disqus_hour_ago,
		disqus_hours_ago = dlcsettings.disqus_hours_ago,
		disqus_day_ago = dlcsettings.disqus_day_ago,
		disqus_days_ago = dlcsettings.disqus_days_ago,
		disqus_week_ago = dlcsettings.disqus_week_ago,
		disqus_weeks_ago = dlcsettings.disqus_weeks_ago,
		disqus_month_ago = dlcsettings.disqus_month_ago,
		disqus_months_ago = dlcsettings.disqus_months_ago,
		disqus_year_ago = dlcsettings.disqus_year_ago,
		disqus_years_ago = dlcsettings.disqus_years_ago,
		disqus_target_blank = dlcsettings.disqus_target_blank;

	if ( disqus_target_blank ) {
		$('.dsq-widget-list a').each(function() {
			var href = $(this).attr( 'href' );
			if ( href ) {
				if ( href.indexOf( window.location.host ) == -1 ) {
					$(this).attr( 'target', '_blank' );
				}
			}
		});
	}

	if ( disqus_minute_ago || disqus_minutes_ago || disqus_hour_ago || disqus_hours_ago || disqus_day_ago || disqus_days_ago || disqus_week_ago || disqus_weeks_ago || disqus_month_ago || disqus_months_ago || disqus_year_ago || disqus_years_ago ) {
		$( '.dsq-widget-list' ).ready( function() {
			$( 'ul.dsq-widget-list p.dsq-widget-meta' ).each( function() {
				var text = $( this ).html();
				if ( disqus_minute_ago ) text = text.replace( "minute ago", disqus_minute_ago )
				if ( disqus_minutes_ago ) text = text.replace( "minutes ago", disqus_minutes_ago )
				if ( disqus_hour_ago ) text = text.replace( "hour ago", disqus_hour_ago )
				if ( disqus_hours_ago ) text = text.replace( "hours ago", disqus_hours_ago )
				if ( disqus_day_ago ) text = text.replace( "day ago", disqus_day_ago )
				if ( disqus_days_ago ) text = text.replace( "days ago", disqus_days_ago )
				if ( disqus_week_ago ) text = text.replace( "week ago", disqus_week_ago )
				if ( disqus_weeks_ago ) text = text.replace( "weeks ago", disqus_weeks_ago )
				if ( disqus_month_ago ) text = text.replace( "month ago", disqus_month_ago )
				if ( disqus_months_ago ) text = text.replace( "months ago", disqus_months_ago )
				if ( disqus_year_ago ) text = text.replace( "year ago", disqus_year_ago )
				if ( disqus_years_ago ) text = text.replace( "years ago", disqus_years_ago )
				$( this ).html( text );
			});
		});
	}

}( jQuery ) );