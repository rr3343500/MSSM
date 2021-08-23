package com.root.mssm.Interface;

import com.root.mssm.List.List.cartitem.Cartdatum;

import java.util.List;

public interface CartChange {
    public  void onCartChange(Boolean blank_status, List<Cartdatum> categories);
}
