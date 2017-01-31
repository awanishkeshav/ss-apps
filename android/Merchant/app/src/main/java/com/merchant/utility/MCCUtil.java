package com.merchant.utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for mobile country code
 * 
 * @author ramanands
 * 
 */
public class MCCUtil {
	private List<MCC> mccList;

	private MCCUtil() {
		mccList = new ArrayList<MCCUtil.MCC>();
		mccList.add(new MCC("Afghanistan", "AF", "AFG", "+93"));
		mccList.add(new MCC("Albania", "AL", "ALB", "+355"));
		mccList.add(new MCC("Algeria", "DZ", "DZA", "+213"));
		mccList.add(new MCC("American Samoa", "AS", "ASM", "+1 684"));
		mccList.add(new MCC("Andorra", "AD", "AND", "+376"));
		mccList.add(new MCC("Angola", "AO", "AGO", "+244"));
		mccList.add(new MCC("Anguilla", "AI", "AIA", "+1 264"));
		mccList.add(new MCC("Antarctica", "AQ", "ATA", "+672"));
		mccList.add(new MCC("Antigua and Barbuda", "AG", "ATG", "+1 268"));
		mccList.add(new MCC("Argentina", "AR", "ARG", "+54"));
		mccList.add(new MCC("Armenia", "AM", "ARM", "+374"));
		mccList.add(new MCC("Aruba", "AW", "ABW", "+297"));
		mccList.add(new MCC("Australia", "AU", "AUS", "+61"));
		mccList.add(new MCC("Austria", "AT", "AUT", "+43"));
		mccList.add(new MCC("Azerbaijan", "AZ", "AZE", "+994"));
		mccList.add(new MCC("Bahamas", "BS", "BHS", "+1 242"));
		mccList.add(new MCC("Bahrain", "BH", "BHR", "+973"));
		mccList.add(new MCC("Bangladesh", "BD", "BGD", "+880"));
		mccList.add(new MCC("Barbados", "BB", "BRB", "+1 246"));
		mccList.add(new MCC("Belarus", "BY", "BLR", "+375"));
		mccList.add(new MCC("Belgium", "BE", "BEL", "+32"));
		mccList.add(new MCC("Belize", "BZ", "BLZ", "+501"));
		mccList.add(new MCC("Benin", "BJ", "BEN", "+229"));
		mccList.add(new MCC("Bermuda", "BM", "BMU", "+1 441"));
		mccList.add(new MCC("Bhutan", "BT", "BTN", "+975"));
		mccList.add(new MCC("Bolivia", "BO", "BOL", "+591"));
		mccList.add(new MCC("Bosnia and Herzegovina", "BA", "BIH", "+387"));
		mccList.add(new MCC("Botswana", "BW", "BWA", "+267"));
		mccList.add(new MCC("Bouvet Island", "BV", "", "+55"));
		mccList.add(new MCC("Brazil", "BR", "BRA", "+55"));
		mccList.add(new MCC("British Indian Ocean Territory", "IO", "IOT", "+246"));
		mccList.add(new MCC("Brunei Darussalam", "BN", "", "+673"));
		mccList.add(new MCC("Bulgaria", "BG", "BGR", "+359"));
		mccList.add(new MCC("Burkina Faso", "BF", "BFA", "+226"));
		mccList.add(new MCC("Burundi", "BI", "BDI", "+257"));
		mccList.add(new MCC("Cambodia", "KH", "KHM", "+855"));
		mccList.add(new MCC("Cameroon", "CM", "CMR", "+237"));
		mccList.add(new MCC("Canada", "CA", "CAN", "+1"));
		mccList.add(new MCC("Cape Verde", "CV", "CPV", "+238"));
		mccList.add(new MCC("Cayman Islands", "KY", "CYM", "+1 345"));
		mccList.add(new MCC("Central African Republic", "CF", "CAF", "+236"));
		mccList.add(new MCC("Chad", "TD", "TCD", "+235"));
		mccList.add(new MCC("Chile", "CL", "CHL", "+56"));
		mccList.add(new MCC("China", "CN", "CHN", "+86"));
		mccList.add(new MCC("Christmas Island", "CX", "CXR", "+61"));
		mccList.add(new MCC("Cocos (Keeling) Islands", "CC", "CCK", "+61"));
		mccList.add(new MCC("Colombia", "CO", "COL", "+57"));
		mccList.add(new MCC("Comoros", "KM", "COM", "+269"));
		mccList.add(new MCC("Congo", "CG", "", "+242"));
		mccList.add(new MCC("Congo, Dem. Republic", "CD", "", "+243"));
		mccList.add(new MCC("Cook Islands", "CK", "COK", "+682"));
		mccList.add(new MCC("Costa Rica", "CR", "CRC", "+506"));
		mccList.add(new MCC("Croatia", "HR", "HRV", "+385"));
		mccList.add(new MCC("Cuba", "CU", "CUB", "+53"));
		mccList.add(new MCC("Cyprus", "CY", "CYP", "+357"));
		mccList.add(new MCC("Czech Republic", "CZ", "CZE", "+420"));
		mccList.add(new MCC("Denmark", "DK", "DNK", "+45"));
		mccList.add(new MCC("Djibouti", "DJ", "DJI", "+253"));
		mccList.add(new MCC("Dominica", "DM", "DMA", "+1 767"));
		mccList.add(new MCC("Dominican Republic", "DO", "DOM", "+1 809"));
		mccList.add(new MCC("Ecuador", "EC", "ECU", "+593"));
		mccList.add(new MCC("Egypt", "EG", "EGY", "+20"));
		mccList.add(new MCC("El Salvador", "SV", "SLV", "+503"));
		mccList.add(new MCC("Equatorial Guinea", "GQ", "GNQ", "+240"));
		mccList.add(new MCC("Eritrea", "ER", "ERI", "+291"));
		mccList.add(new MCC("Estonia", "EE", "EST", "+372"));
		mccList.add(new MCC("Ethiopia", "ET", "ETH", "+251"));
		mccList.add(new MCC("European Union", "EU.INT", "", "+00"));
		mccList.add(new MCC("Falkland Islands", "FK", "FLK", "+500"));
		mccList.add(new MCC("Faroe Islands", "FO", "FRO", "+298"));
		mccList.add(new MCC("Fiji", "FJ", "FJI", "+679"));
		mccList.add(new MCC("Finland", "FI", "FIN", "+358"));
		mccList.add(new MCC("France", "FR", "FRA", "+33"));
		mccList.add(new MCC("French Guiana", "GF", "", "+594"));
		mccList.add(new MCC("French Southern Territories", "TF", "", "+262"));
		mccList.add(new MCC("Gabon", "GA", "GAB", "+241"));
		mccList.add(new MCC("Gambia", "GM", "GMB", "+220"));
		mccList.add(new MCC("Georgia", "GE", "GEO", "+995"));
		mccList.add(new MCC("Germany", "DE", "DEU", "+49"));
		mccList.add(new MCC("Ghana", "GH", "GHA", "+233"));
		mccList.add(new MCC("Gibraltar", "GI", "GIB", "+350"));
		mccList.add(new MCC("Great Britain", "GB", "", "+44"));
		mccList.add(new MCC("Greece", "GR", "GRC", "+30"));
		mccList.add(new MCC("Greenland", "GL", "GRL", "+299"));
		mccList.add(new MCC("Grenada", "GD", "GRD", "+1 473"));
		mccList.add(new MCC("Guadeloupe (French)", "GP", "", "+590"));
		mccList.add(new MCC("Guam(USA)", "GU", "GUM", "+1 671"));
		mccList.add(new MCC("Guatemala", "GT", "GTM", "+502"));
		mccList.add(new MCC("Guernsey", "GG", "", "+44"));
		mccList.add(new MCC("Guinea", "GN", "GIN", "+224"));
		mccList.add(new MCC("Guinea-Bissau", "GW", "GNB", "+245"));
		mccList.add(new MCC("Guyana", "GY", "GUY", "+592"));
		mccList.add(new MCC("Haiti", "HT", "HTI", "+509"));
		mccList.add(new MCC("Heard Island and McDonald Islands", "HM", "", "+0"));
		mccList.add(new MCC("Honduras", "HN", "HND", "+504"));
		mccList.add(new MCC("Hong Kong", "HK", "HKG", "+852"));
		mccList.add(new MCC("Hungary", "HU", "HUN", "+36"));
		mccList.add(new MCC("Iceland", "IS", "IS", "+354"));
		mccList.add(new MCC("India", "IN", "IND", "+91"));
		mccList.add(new MCC("Indonesia", "ID", "IDN", "+62"));
		mccList.add(new MCC("Iran", "IR", "IRN", "+98"));
		mccList.add(new MCC("Iraq", "IQ", "IRQ", "+964"));
		mccList.add(new MCC("Ireland", "IE", "IRL", "+353"));
		mccList.add(new MCC("Isle of Man", "IM", "IMN", "+44"));
		mccList.add(new MCC("Israel", "IL", "ISR", "+972"));
		mccList.add(new MCC("Italy", "IT", "ITA", "+39"));
		mccList.add(new MCC("Ivory Coast", "CI", "CIV", "+225"));
		mccList.add(new MCC("Jamaica", "JM", "JAM", "+1 876"));
		mccList.add(new MCC("Japan", "JP", "JPN", "+81"));
		mccList.add(new MCC("Jersey", "JE", "JEY", "+44"));
		mccList.add(new MCC("Jordan", "JO", "JOR", "+962"));
		mccList.add(new MCC("Kazakhstan", "KZ", "KAZ", "+7"));
		mccList.add(new MCC("Kenya", "KE", "KEN", "+254"));
		mccList.add(new MCC("Kiribati", "KI", "KIR", "+686"));
		mccList.add(new MCC("Korea-North", "KP", "", "+850"));
		mccList.add(new MCC("Korea-South", "KR", "", "+82"));
		mccList.add(new MCC("Kuwait", "KW", "", "+965"));
		mccList.add(new MCC("Kyrgyzstan", "KG", "KGZ", "+996"));
		mccList.add(new MCC("Laos", "LA", "LAO", "+856"));
		mccList.add(new MCC("Latvia", "LV", "LVA", "+371"));
		mccList.add(new MCC("Lebanon", "LB", "LBN", "+961"));
		mccList.add(new MCC("Lesotho", "LS", "LSO", "+266"));
		mccList.add(new MCC("Liberia", "LR", "LBR", "+231"));
		mccList.add(new MCC("Libya", "LY", "LBY", "+218"));
		mccList.add(new MCC("Liechtenstein", "LI", "LIE", "+423"));
		mccList.add(new MCC("Lithuania", "LT", "LTU", "+370"));
		mccList.add(new MCC("Luxembourg", "LU", "LUX", "+352"));
		mccList.add(new MCC("Macau", "MO", "MAC", "+853"));
		mccList.add(new MCC("Macedonia", "MK", "MKD", "+389"));
		mccList.add(new MCC("Madagascar", "MG", "MDG", "+261"));
		mccList.add(new MCC("Malawi", "MW", "MWI", "+265"));
		mccList.add(new MCC("Malaysia", "MY", "MYS", "+60"));
		mccList.add(new MCC("Maldives", "MV", "MDV", "+960"));
		mccList.add(new MCC("Mali", "ML", "MLI", "+223"));
		mccList.add(new MCC("Malta", "MT", "MLT", "+356"));
		mccList.add(new MCC("Marshall Islands", "MH", "MHL", "+692"));
		mccList.add(new MCC("Martinique (French)", "MQ", "", "+596"));
		mccList.add(new MCC("Mauritania", "MR", "MRT", "+222"));
		mccList.add(new MCC("Mauritius", "MU", "MUS", "+230"));
		mccList.add(new MCC("Mayotte", "YT", "MYT", "+262"));
		mccList.add(new MCC("Mexico", "MX", "MEX", "+52"));
		mccList.add(new MCC("Micronesia", "FM", "FSM", "+691"));
		mccList.add(new MCC("Moldova", "MD", "MDA", "+373"));
		mccList.add(new MCC("Monaco", "MC", "MCO", "+377"));
		mccList.add(new MCC("Mongolia", "MN", "MNG", "+976"));
		mccList.add(new MCC("Montenegro", "ME", "MNE", "+382"));
		mccList.add(new MCC("Montserrat", "MS", "MSR", "+1 664"));
		mccList.add(new MCC("Morocco", "MA", "MAR", "+212"));
		mccList.add(new MCC("Mozambique", "MZ", "MOZ", "+258"));
		mccList.add(new MCC("Myanmar", "MM", "", "+95"));
		mccList.add(new MCC("Namibia", "NA", "NAM", "+264"));
		mccList.add(new MCC("Nauru", "NR", "NRU", "+674"));
		mccList.add(new MCC("Nepal", "NP", "NPL", "+977"));
		mccList.add(new MCC("Netherlands", "NL", "NLD", "+31"));
		mccList.add(new MCC("Netherlands Antilles", "AN", "ANT", "+599"));
		mccList.add(new MCC("New Caledonia", "NC", "NCL", "+687"));
		mccList.add(new MCC("New Zealand", "NZ", "NZL", "+64"));
		mccList.add(new MCC("Nicaragua", "NI", "NIC", "+505"));
		mccList.add(new MCC("Niger", "NE", "NER", "+227"));
		mccList.add(new MCC("Nigeria", "NG", "NGA", "+234"));
		mccList.add(new MCC("Niue", "NU", "NIU", "+683"));
		mccList.add(new MCC("Norfolk Island", "NI", "NFK", "+672"));
		mccList.add(new MCC("Northern Mariana Islands", "MP", "MNP", "+1 670"));
		mccList.add(new MCC("Norway", "NO", "NOR", "+47"));
		mccList.add(new MCC("Oman", "OM", "OMN", "+968"));
		mccList.add(new MCC("Pakistan", "PK", "PAK", "+92"));
		mccList.add(new MCC("Palau", "PW", "PLW", "+680"));
		mccList.add(new MCC("Panama", "PA", "PAN", "+507"));
		mccList.add(new MCC("Papua New Guinea", "PG", "PNG", "+675"));
		mccList.add(new MCC("Paraguay", "PY", "PRY", "+595"));
		mccList.add(new MCC("Peru", "PE", "PER", "+51"));
		mccList.add(new MCC("Philippines", "PH", "PHL", "+63"));
		mccList.add(new MCC("Pitcairn Islands", "PN", "PCN", "+870"));
		mccList.add(new MCC("Poland", "PL", "POL", "+48"));
		mccList.add(new MCC("Polynesia (French)", "PF", "", "+689"));
		mccList.add(new MCC("Portugal", "PT", "PRT", "+351"));
		mccList.add(new MCC("Puerto Rico", "PR", "PRI", "+1"));
		mccList.add(new MCC("Qatar", "QA", "QAT", "+974"));
		mccList.add(new MCC("Reunion (French)", "RE", "", "+262"));
		mccList.add(new MCC("Romania", "RO", "ROU", "+40"));
		mccList.add(new MCC("Russia", "RU", "RUS", "+7"));
		mccList.add(new MCC("Rwanda", "RW", "RWA", "+250"));
		mccList.add(new MCC("Saint Helena", "SH", "SHN", "+290"));
		mccList.add(new MCC("Saint Kitts & Nevis Anguilla", "KN", "KNA", "+1 869"));
		mccList.add(new MCC("Saint Lucia", "LC", "LCA", "+1 758"));
		mccList.add(new MCC("Saint Pierre and Miquelon", "PM", "SPM", "+508"));
		mccList.add(new MCC("Saint Vincent and the Grenadines", "VC", "VCT", "+1 784"));
		mccList.add(new MCC("Samoa", "WS", "WSM", "+685"));
		mccList.add(new MCC("San Marino", "SM", "SMR", "+378"));
		mccList.add(new MCC("Sao Tome and Principe", "ST", "STP", "+239"));
		mccList.add(new MCC("Saudi Arabia", "SA", "SAU", "+966"));
		mccList.add(new MCC("Senegal", "SN", "SEN", "+221"));
		mccList.add(new MCC("Serbia", "RS", "SRB", "+381"));
		mccList.add(new MCC("Seychelles", "SC", "SYC", "+248"));
		mccList.add(new MCC("Sierra Leone", "SL", "SLE", "+232"));
		mccList.add(new MCC("Singapore", "SG", "SGP", "+65"));
		mccList.add(new MCC("Slovakia", "SK", "SVK", "+421"));
		mccList.add(new MCC("Slovenia", "SI", "SVN", "+386"));
		mccList.add(new MCC("Solomon Islands", "SB", "SLB", "+677"));
		mccList.add(new MCC("Somalia", "SO", "SOM", "+252"));
		mccList.add(new MCC("South Africa", "ZA", "ZAF", "+27"));
		mccList.add(new MCC("South Georgia & South Sandwich Islands", "GS", "", "+0"));
		mccList.add(new MCC("South Sudan", "SS", "", "+211"));
		mccList.add(new MCC("Spain", "ES", "ESP", "+34"));
		mccList.add(new MCC("Sri Lanka", "LK", "LKA", "+94"));
		mccList.add(new MCC("Sudan", "SD", "SDN", "+249"));
		mccList.add(new MCC("Suriname", "SR", "SUR", "+597"));
		mccList.add(new MCC("Svalbard and Jan Mayen Islands", "SJ", "SJM", "+47"));
		mccList.add(new MCC("Swaziland", "SZ", "SWZ", "+268"));
		mccList.add(new MCC("Sweden", "SE", "SWE", "+46"));
		mccList.add(new MCC("Switzerland", "CH", "CHE", "+41"));
		mccList.add(new MCC("Syria", "SY", "SYR", "+963"));
		mccList.add(new MCC("Taiwan", "TW", "TWN", "+886"));
		mccList.add(new MCC("Tajikistan", "TJ", "TJK", "+992"));
		mccList.add(new MCC("Tanzania", "TZ", "TZA", "+255"));
		mccList.add(new MCC("Thailand", "TH", "THA", "+66"));
		mccList.add(new MCC("Togo", "TG", "TGO", "+228"));
		mccList.add(new MCC("Tokelau", "TK", "TKL", "+690"));
		mccList.add(new MCC("Tonga", "TO", "TON", "+676"));
		mccList.add(new MCC("Trinidad and Tobago", "TT", "TTO", "+1 868"));
		mccList.add(new MCC("Tunisia", "TN", "TUN", "+216"));
		mccList.add(new MCC("Turkey", "TR", "TUR", "+90"));
		mccList.add(new MCC("Turkmenistan", "TM", "TKM", "+993"));
		mccList.add(new MCC("Turks and Caicos Islands", "TC", "TCA", "+1 649"));
		mccList.add(new MCC("Tuvalu", "TV", "TUV", "+688"));
		mccList.add(new MCC("U.K.", "UK", "", "+44"));
		mccList.add(new MCC("Uganda", "UG", "UGA", "+256"));
		mccList.add(new MCC("Ukraine", "UA", "UKR", "+380"));
		mccList.add(new MCC("Uruguay", "UY", "URY", "+598"));
		mccList.add(new MCC("USA", "US", "", "+1"));
		mccList.add(new MCC("USA Minor Outlying Islands", "UM", "", "+1"));
		mccList.add(new MCC("Uzbekistan", "UZ", "UZB", "+998"));
		mccList.add(new MCC("Vanuatu", "VU", "VUT", "+678"));
		mccList.add(new MCC("Vatican", "VA", "", "+39"));
		mccList.add(new MCC("Venezuela", "VE", "VEN", "+58"));
		mccList.add(new MCC("Vietnam", "VN", "VNM", "+84"));
		mccList.add(new MCC("Virgin Islands (British)", "VG", "", "+1 284"));
		mccList.add(new MCC("Virgin Islands (USA)", "VI", "", "+1 340"));
		mccList.add(new MCC("Wallis and Futuna", "WF", "WLF", "+681"));
		mccList.add(new MCC("Western Sahara", "EH", "ESH", "+212"));
		mccList.add(new MCC("Yemen", "YE", "YEM", "+967"));
		mccList.add(new MCC("Zambia", "ZM", "ZMB", "+260"));
		mccList.add(new MCC("Zimbabwe", "ZW", "ZWE", "+263"));
	}

	public class MCC {
		public String country;
		public String twoChar;
		public String threeChar;
		public String code;

		public MCC(String country, String twoChar, String threeChar, String code) {
			super();
			this.country = country;
			this.twoChar = twoChar;
			this.threeChar = threeChar;
			this.code = code;
		}

	}

	private static MCCUtil mccUtil = null;

	public static MCCUtil getInstance() {
		if (null == mccUtil) {
			mccUtil = new MCCUtil();
		}
		return mccUtil;
	}

	public void deleteInstance() {
		mccUtil = null;
	}

	public List<MCC> getMCCList() {
		return mccList;
	}
}
