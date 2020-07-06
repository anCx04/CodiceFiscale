package it.crescenziandrea.codicefiscale;

public final class IntentResult {

    private final String contents;
    private final String formatName;
    private final byte[] rawBytes;
    private final Integer orientation;
    private final String errorCorrectionLevel;

    public IntentResult() {
        this(null, null, null, null, null);
    }

    public IntentResult(String contents,
                 String formatName,
                 byte[] rawBytes,
                 Integer orientation,
                 String errorCorrectionLevel) {
        this.contents = contents;
        this.formatName = formatName;
        this.rawBytes = rawBytes;
        this.orientation = orientation;
        this.errorCorrectionLevel = errorCorrectionLevel;
    }

    public String getContents() {
        return contents;
    }

    public String getFormatName() {
        return formatName;
    }

    public byte[] getRawBytes() {
        return rawBytes;
    }

    public Integer getOrientation() {
        return orientation;
    }

    public String getErrorCorrectionLevel() {
        return errorCorrectionLevel;
    }

    @Override
    public String toString() {
        int rawBytesLength = rawBytes == null ? 0 : rawBytes.length;
        return "Format: " + formatName + '\n' +
                "Contents: " + contents + '\n' +
                "Raw bytes: (" + rawBytesLength + " bytes)\n" +
                "Orientation: " + orientation + '\n' +
                "EC level: " + errorCorrectionLevel + '\n';
    }

}