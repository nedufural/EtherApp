package com.fastcon.etherapp.ui.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fastcon.etherapp.R;
import com.fastcon.etherapp.data.local.PrefUtils;
import com.fastcon.etherapp.util.common.Commons;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.zxing.WriterException;

public class QrCodeAddressImage extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_fragment, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       ImageView address = view.findViewById(R.id.address_qr_code);
        TextView addressTxt = view.findViewById(R.id.currency_display_title);

        try {

            if(PrefUtils.getCurrencyDisplayID() == 1){
                addressTxt.setText("BITCOIN");
            address.setImageBitmap(Commons.encodeAsBitmap(PrefUtils.getBitcoinAddress()));}
            else if(PrefUtils.getCurrencyDisplayID() == 2){
                addressTxt.setText("ETHEREUM");
                address.setImageBitmap(Commons.encodeAsBitmap(PrefUtils.getEtherAddress()));}
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
