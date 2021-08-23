package com.root.mssm.Interface;

import android.location.Address;

import java.io.IOException;
import java.util.List;

public interface CurrentLocation {
    void onSuccess(List<Address> location);

    void onFailure(IOException error);
}
