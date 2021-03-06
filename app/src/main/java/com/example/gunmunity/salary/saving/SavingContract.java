package com.example.gunmunity.salary.saving;

import java.util.List;

public interface SavingContract {

    interface View{
        public void updateView(SavingModel model);
        public void updateView(List<SavingCurModel> model);
    }

    interface Presenter {
        void setView(View view);
        void createModel();
        int loaditem(int code);
    }
}
