package simemens;

/**
 * @author bo.zhou
 * @date 2019/11/21
 */
public class AnalysisData {
    public int ErrorCode = 0;
    public Integer type;
    public Integer offset;
    public Integer dbLabel;

    public int getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(int errorCode) {
        ErrorCode = errorCode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getDbLabel() {
        return dbLabel;
    }

    public void setDbLabel(Integer dbLabel) {
        this.dbLabel = dbLabel;
    }
}
