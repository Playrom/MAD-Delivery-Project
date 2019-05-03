package it.polito.justorder_framework.abstract_activities;

public class AbstractListViewWithSidenav extends ActivityAbstractWithSideNav {
    @Override
    protected void setupActivity() {
        super.setupActivity();
        this.initDataSource();
    }

    protected void initDataSource() {}
}