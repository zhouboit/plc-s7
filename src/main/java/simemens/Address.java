package simemens;

/**
 * @author bo.zhou
 * @date 2019/11/21
 */
public class Address {
    public static AnalysisData analysis(String address) {
        AnalysisData result = new AnalysisData();
        try {
            result.setDbLabel(0);
            char dbCode = address.charAt(0);
            switch (dbCode) {
                case 'I':
                    result.setType(0x81);
                    result.setOffset(calculateAddressStarted(address.substring(1)));
                    break;
                case 'Q':
                    result.setType(0x82);
                    result.setOffset(calculateAddressStarted(address.substring(1)));
                    break;
                case 'M':
                    result.setType(0x83);
                    result.setOffset(calculateAddressStarted(address.substring(1)));
                    break;
                case 'D':
                    result.setType(0x84);
                    result.setOffset(calculateAddressStarted(address.substring(address.indexOf(".") + 1)));
                    String[] adds = address.split("\\.");
                    if ("DB".equals(address.substring(0, 2))) {
                        result.setDbLabel(calculateAddressStarted(adds[0].substring(2)));
                    } else {
                        result.setDbLabel(calculateAddressStarted(adds[0].substring(1)));
                    }
                    break;
                case 'T':
                    result.setType(0x1D);
                    result.setOffset(calculateAddressStarted(address.substring(1)));
                    break;
                case 'C':
                    result.setType(0x1C);
                    result.setOffset(calculateAddressStarted(address.substring(1)));
                    break;
                case 'V':
                    result.setType(0x84);
                    result.setOffset(calculateAddressStarted(address.substring(1)));
                    result.setDbLabel(1);
                    break;
                default:
                    result.setErrorCode(-1);
                    result.setType(0);
                    result.setOffset(0);
                    result.setDbLabel(0);
                    return result;


            }
        } catch (Exception e) {
            result.setErrorCode(1);
            return result;
        }
        return result;
    }

    private static int calculateAddressStarted(String address) {
        if (address.contains(".")) {
            String[] str = address.split("\\.");
            return Integer.parseInt(str[0]) * 8 + Integer.parseInt(str[1]);
        } else {
            return Integer.parseInt(address) * 8;
        }
    }
}
