package ru.pavlik.chempred.client.application.train;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
import ru.pavlik.chempred.client.model.dao.CompoundDao;
import ru.pavlik.chempred.client.model.dao.DescriptorType;
import ru.pavlik.chempred.client.model.rpc.ErrorHandlerCallback;
import ru.pavlik.chempred.client.services.compound.CompoundService;
import ru.pavlik.chempred.client.services.compound.CompoundServiceAsync;
import ru.pavlik.chempred.client.services.descriptor.DescriptorService;
import ru.pavlik.chempred.client.services.descriptor.DescriptorServiceAsync;
import ru.pavlik.chempred.client.services.prediction.PredictionService;
import ru.pavlik.chempred.client.services.prediction.PredictionServiceAsync;
import ru.pavlik.chempred.client.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainPresenter extends PresenterWidget<TrainPresenter.MyView> implements TrainUiHandler {

    private CompoundServiceAsync compoundService;
    private DescriptorServiceAsync descriptorService;
    private PredictionServiceAsync predictionService;

    interface MyView extends PopupView, HasUiHandlers<TrainUiHandler> {

        void showCompounds(List<CompoundDao> compounds);

        void showTables(DescriptorType type, List<String> descriptors);

        void addCompound(CompoundDao compound, Map<String, String> descriptors);

        void showTrainProgress();

        void hideTrainProgress();
    }

    public TrainPresenter(EventBus eventBus, MyView view) {
        super(eventBus, view);
        getView().setUiHandlers(this);
        compoundService = CompoundService.Service.getInstance();
        descriptorService = DescriptorService.Service.getInstance();
        predictionService = PredictionService.Service.getInstance();
    }

    @Override
    public void loadCompounds() {
        compoundService.getCompounds(new ErrorHandlerCallback<List<CompoundDao>>() {
            @Override
            public void onSuccess(List<CompoundDao> result) {
                getView().showCompounds(result);
            }
        });
    }

    @Override
    public void loadSourceDescriptors() {
        descriptorService.getSourceDescriptors(new AsyncCallback<Map<DescriptorType, List<String>>>() {
            @Override
            public void onFailure(Throwable caught) {
                Utils.console(caught);
            }

            @Override
            public void onSuccess(Map<DescriptorType, List<String>> result) {
                for (Map.Entry<DescriptorType, List<String>> entry : result.entrySet()) {
                    getView().showTables(entry.getKey(), entry.getValue());
                }
            }
        });
    }

    @Override
    public void searchCompound(String query) {
        compoundService.findCompounds(query, new ErrorHandlerCallback<List<CompoundDao>>() {
            @Override
            public void onSuccess(List<CompoundDao> result) {
                getView().showCompounds(result);
            }
        });
    }

    @Override
    public void loadCompoundDescriptors(CompoundDao compoundDao) {
        descriptorService.getCompoundDescriptors(compoundDao, new AsyncCallback<Map<String, Integer>>() {
            @Override
            public void onFailure(Throwable caught) {
                Utils.console(caught);
            }

            @Override
            public void onSuccess(Map<String, Integer> result) {
                Map<String, String> countDescriptors = new HashMap<>();

                for (Map.Entry<String, Integer> entry : result.entrySet()) {
                    countDescriptors.put(entry.getKey(), String.valueOf(entry.getValue()));
                }

                getView().addCompound(compoundDao, countDescriptors);
            }
        });
    }

    @Override
    public void handlerTrain(List<CompoundDao> compounds) {
        getView().showTrainProgress();
        predictionService.train(compounds, new AsyncCallback<Double>() {
            @Override
            public void onFailure(Throwable caught) {
                Utils.console(caught);
                getView().hideTrainProgress();
            }

            @Override
            public void onSuccess(Double result) {
                Utils.console("Total error: " + result);
                getView().hideTrainProgress();
            }
        });
    }
}