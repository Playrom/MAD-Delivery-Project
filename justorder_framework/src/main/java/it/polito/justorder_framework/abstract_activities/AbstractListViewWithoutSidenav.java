package it.polito.justorder_framework.abstract_activities;

public class AbstractListViewWithoutSidenav extends ActivityAbstractWithToolbar {
    @Override
    protected void setupActivity() {
        super.setupActivity();
        this.initDataSource();
    }

    protected void initDataSource() {}
}