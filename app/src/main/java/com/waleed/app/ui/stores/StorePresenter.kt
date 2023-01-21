package com.waleed.app.ui.stores


import com.waleed.app.business.data.StoreData
import com.waleed.app.ui.base.BasePresenter
import javax.inject.Inject

class StorePresenter @Inject constructor():BasePresenter<StoresView>() {
    fun getStoreData() {
        var data1=StoreData(1,"Salmiya","Bldg. 116, Block 2, Salem Al Mubarak St., 5th Floor, Opp.  Olympia Towers, Salmiya, Kuwait.",true,"5 PM",false)
        var data2=StoreData(1,"Farwaniyah","Bldg. 116, Block 2, Salem Al Mubarak St., 5th Floor, Opp.  Olympia Towers, Salmiya, Kuwait.",false,"9 PM",false)
        var data3=StoreData(1,"Fhaheel","Bldg. 116, Block 2, Salem Al Mubarak St., 5th Floor, Opp.  Olympia Towers, Salmiya, Kuwait.",false,"10 AM",false)
        var data4=StoreData(1,"Fintas","Bldg. 116, Block 2, Salem Al Mubarak St., 5th Floor, Opp.  Olympia Towers, Salmiya, Kuwait.",true,"5 PM",false)
        var data5=StoreData(1,"Kuwait City","Bldg. 116, Block 2, Salem Al Mubarak St., 5th Floor, Opp.  Olympia Towers, Salmiya, Kuwait.",true,"5 PM",false)
        var data6=StoreData(1,"Hawally","Bldg. 116, Block 2, Salem Al Mubarak St., 5th Floor, Opp.  Olympia Towers, Salmiya, Kuwait.",false,"8 AM",false)
        var listData= mutableListOf<StoreData>(data1,data2,data3,data4,data5,data6)
        view?.showStoreList(listData)
    }
}