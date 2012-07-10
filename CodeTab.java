
public class CodeTab {
	public final static String[] GET_ID = {"52", "Request Device Identification"};
	public final static String[] ICC = {"51", "Initialize Connection (ICC)"};
	public final static String[] NAK = {"15"};
	public final static String[] GET_DATA = {"24", "Request current DATA"};
	public final static String[] REQ_DATE = {"28", "Request current DATE & TIME"};
	public final static String[] REQ_SETTINGS = {"29", "Request current DEVICE SETTINGS"};
	public final static String[] REQ_MSGS = {"2A", "Request current TEXT MESSAGES"};
	public final static String[] REQ_ALARMS = {"2E", "Request current ALARMS"};
	public final static String[] NOP = {"30", "Do nothing (NOP)"};
	public final static String[] CONF_DATA_RESP = {"4A", "Configure Data Response"};
	public final static String[] REQ_CONF_REALTIME = {"53", "Request Realtime Configuration"};
	public final static String[] CONF_REALTIME = {"54", "Configure Realtime Transmission"};
	public final static String[] STOP_COMM = {"55", "Stop Communication"};
	public final static String[] BREATHING_PRESSURE = {"05", "Breathing Pressure", "mbar", "*_XX_", "0", "1", "1"};
	public final static String[] COMPLIANCE = {"06", "Compliance", "mL/mbar", "XX.X", "1", "0", "0"};
	public final static String[] MEAN_BREATHING_PRESSURE = {"73", "Mean Breathing Pressure", "mbar", "*_XX_", "1", "0", "0"};
	public final static String[] PLATEAU_PRESSURE = {"05", "Plateau Pressure", "mbar", "*_XX_", "1", "0", "0"};
	public final static String[] PEEP = {"78", "PEEP Breathing Pressure", "mbar", "*_XX_", "1", "0", "0"};
	public final static String[] PEAK_PRESSURE = {"7D", "Peak Breathing Pressure", "mbar", "*_XX_", "1", "0", "0"};
	public final static String[] TIDAL_VOLUME = {"88", "Tidal Volume", "mL", "XXXX", "1", "0", "0"};
	public final static String[] INSP_TIDAL_VOLUME = {"8B", "Insp. Tidal Volume", "mL", "XXXX", "1", "0", "0"};
	public final static String[] RESPIRATORY_RATE_PCV = {"B4", "Respiratory Rate (Pressure)", "1/min", "XX__", "1", "0", "0"};
	public final static String[] RESPIRATORY_MINUTE_VOL = {"B9", "Respiratory Minute Volume", "L", "XX.X", "1", "1", "1"};
	public final static String[] APNEA_DURATION = {"BD", "Apnea Duration", "sec", "XXX_", "1", "0", "0"};
	public final static String[] RESPIRATORY_RATE_VCV = {"D7", "Respiratory Rate (Volume/Flow)", "1/min", "XX__", "1", "0", "0"};
	public final static String[] RESPIRATORY_RATE_DER = {"D9", "Respiratory Rate (derived)", "1/min", "XX__", "1", "0", "0"};
	public final static String[] RESPIRATORY_RATE_CO2 = {"D5", "Respiratory Rate (Pressure)"};
	
	
	
	public final static String[][] CODE = {
	{"52", "Request Device Identification"},
	{"51", "Initialize Connection (ICC)"},
	{"15", "NAK"},
	{"24", "Request current DATA"},
	{"28", "Request current DATE & TIME"},
	{"29", "Request current DEVICE SETTINGS"},
	{"2A", "Request current TEXT MESSAGES"},
	{"2E", "Request current ALARMS"},
	{"30", "Do nothing (NOP)"},
	{"4A", "Configure Data Response"},
	{"53", "Request Realtime Configuration"},
	{"54", "Configure Realtime Transmission"},
	{"55", "Stop Communication"},
	{"05", "Breathing Pressure", "mbar", "*_XX_", "0", "1", "1"},
	{"06", "Compliance", "mL/mbar", "XX.X", "1", "0", "0"},
	{"73", "Mean Breathing Pressure", "mbar", "*_XX_", "1", "0", "0"},
	{"05", "Plateau Pressure", "mbar", "*_XX_", "1", "0", "0"},
	{"78", "PEEP Breathing Pressure", "mbar", "*_XX_", "1", "0", "0"},
	{"7D", "Peak Breathing Pressure", "mbar", "*_XX_", "1", "0", "0"},
	{"88", "Tidal Volume", "mL", "XXXX", "1", "0", "0"},
	{"8B", "Insp. Tidal Volume", "mL", "XXXX", "1", "0", "0"},
	{"B4", "Respiratory Rate (Pressure)", "1/min", "XX__", "1", "0", "0"},
	{"B9", "Respiratory Minute Volume", "L", "XX.X", "1", "1", "1"},
	{"BD", "Apnea Duration", "sec", "XXX_", "1", "0", "0"},
	{"D7", "Respiratory Rate (Volume/Flow)", "1/min", "XX__", "1", "0", "0"},
	{"D9", "Respiratory Rate (derived)", "1/min", "XX__", "1", "0", "0"},
	{"D5", "Respiratory Rate (CO2)", "1/min", "XX__", "1", "0", "0"},
	{"E6", "Endtiidal CO2 in mmHg", "mmHg", "XX__", "1", "1", "1"}};
}
