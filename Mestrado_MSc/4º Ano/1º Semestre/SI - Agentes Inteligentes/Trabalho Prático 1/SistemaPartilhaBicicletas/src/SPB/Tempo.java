package SPB;

import java.time.LocalDateTime;

public class Tempo {
	public Tempo() {}

	public static String get_time() {
		StringBuilder sb = new StringBuilder();
		sb.append( "[" + LocalDateTime.now().getYear() + "-");
		sb.append( LocalDateTime.now().getMonthValue() + "-");
		sb.append( LocalDateTime.now().getDayOfMonth() + " ");
		sb.append( LocalDateTime.now().getHour() + ":" );
		sb.append( LocalDateTime.now().getMinute() + ":");
		sb.append( LocalDateTime.now().getSecond() + "] ");
		
		return sb.toString();		
	}
}
