package it.crescenziandrea.codicefiscale;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity
public class ProDis implements Parcelable {

    @SerializedName("nome")
    private String proDis;

    @SerializedName("codiceCatastale")
    private String codCat;


    protected ProDis(Parcel in) {
        proDis = in.readString();
        codCat = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(proDis);
        dest.writeString(codCat);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProDis> CREATOR = new Creator<ProDis>() {
        @Override
        public ProDis createFromParcel(Parcel in) {
            return new ProDis(in);
        }

        @Override
        public ProDis[] newArray(int size) {
            return new ProDis[size];
        }
    };

    public String getProDis() {
        return proDis;
    }

    public void setProDis(String proDis) {
        this.proDis = proDis;
    }

    public String getCodCat() {
        if(codCat != null)
            return codCat;
        else
            return "null";
    }

    public void setCodCat(String codCat) {
        this.codCat = codCat;
    }
}
