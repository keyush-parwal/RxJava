package org.example;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observables.ConnectableObservable;
import io.reactivex.rxjava3.observers.ResourceObserver;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static int start=5,count=2;

    public static void main(String[] args) {

        CreateObserVableWithJust();
        CreateObserVableFromIterable();
        CreateObserVableUsingCreate();


        createColdObservable();
        createHotAndColdConnectableObservable();


        throwException();
        throwExceptionUsingCallable();


        createObservableUsingEmpty();
        createObservableUsingNever();


        Observable<Integer>observable=Observable.range(5,3);
        observable.subscribe(item-> System.out.println(item));


        Observable<Integer>observable2=Observable.defer(()->Observable.range(start,count));
        observable2.subscribe(item-> System.out.println("Observer 1: "+item));

        count=3;
        observable2.subscribe(item-> System.out.println("Observer 2: "+item));


        Observable<Integer>observable3=Observable.fromCallable(()->getNumber());

        observable3.subscribe(System.out::println,
                error-> System.out.println("An Exception occur "+error.getLocalizedMessage()));


        Observable observable4=Observable.interval(1, TimeUnit.SECONDS);

        observable4.subscribe(item-> System.out.println("Observable 1: "+item));
        pause(2000);

        observable4.subscribe(item-> System.out.println("Observable 2: "+item));
        pause(3000);


        ConnectableObservable<Long> observable5= Observable.interval(1, TimeUnit.SECONDS).publish();
        observable5.subscribe(item->System.out.println("1st Observer :"+item));

        observable5.connect();
        pause(5000);
        observable5.subscribe(item->System.out.println("2nd Observer :"+item));
        pause(3000);


        createSingle();
        createMayBe();
        createCompletable();


        handleDisposable();
        handleDisposableInObserver();
        handleDisposableOutsideObserver();
        compositeDisposable();


        mapOperator();
        filterOperator();
        combineMapAndFilterOpeartor();


        takeOperator();
        takeWithInterval();
        takeWhileOperator();
        skipOperator();
        skipWhileOperator();


        distinctOperator();
        distinctWithKeySelector();
        distinctUntilChangeOperator();
        distinctUntilChangeWithKeyOperator();


        useDefaultIfEmpty();
        useSwitchIfEmpty();


        useRepeat();
        useScan();
        useScanWithInitialValue();


        useSorted();
        useSortedWithOwnOperator();
        useSortedOnNOnComparator();


        useDelay();
        delayError();


        containsWithPremetive();
        containsWithNonPremetive();


        exDoOnError();
        exOnErrorResumeNext();
        exOnErrorReturn();
        exOnErrorReturnItem();


        retryWithPredicate();
        exRetry();
        exRetryUntil();


        exDoOnSubscribe();
        exDoOnNext();
        exDoOnComplete();


        DoFinally();


        exMerge();
        exMergeArray();
        exMergeItrable();
        exMergeWith();
        exmergeInfinite();


        exZip();
        exZipWith();
        exZipInterable();


        exFlatMap();
        exFlatMapBiFunction();


        exConcat();
        exConcatWith();
        exConcatMap();

    }

    private static void exConcatMap() {
        Observable.just("ball","cat","horse")
                .concatMap(item->Observable.fromArray(item.split("")))
                .subscribe(item-> System.out.println(item));
    }

    private static void exConcatWith() {
        Observable<String>ob1=Observable.interval(1,TimeUnit.SECONDS)
                .take(5)
                .map(item->"Ob1 : "+item);

        Observable<String>ob2=Observable.interval(300,TimeUnit.MILLISECONDS)
                .map(item->"Ob2 : "+item);

        ob1.concatWith(ob2)
                .subscribe(item-> System.out.println(item));

        pause(10000);
    }

    private static void exConcat() {
        Observable<Integer>ob1=Observable.range(1,5);
        Observable<Integer>ob2=Observable.range(6,5);

        Observable.concat(ob1,ob2)
                .subscribe(item-> System.out.println(item));
    }

    private static void exFlatMapBiFunction() {
        Observable<String>observable=Observable.just("foo","bar","jam");

        observable
                .flatMap((data)-> Observable.fromArray(data.split("")),
                        (actual,second)->actual+" "+second)
                .subscribe(item-> System.out.println(item));
    }

    private static void exFlatMap() {
        Observable<String>observable=Observable.just("foo","bar","jam");

        observable
                .flatMap((data)->{
                    if(data.equals("bar")){
                        return Observable.empty();
                    }
                    else {
                        return Observable.fromArray(data.split(""));
                    }
                })
                .subscribe(item-> System.out.println(item));
    }

    private static void exZipInterable() {
        Observable<Integer>ob1=Observable.just(1,2,3,4,5);
        Observable<Integer>ob2=Observable.range(6,5);
        Observable<Integer>ob3=Observable.fromIterable(Arrays.asList(11,12,13,14,15));

        List<Observable<Integer>>observableList=Arrays.asList(ob1,ob2,ob3);

        Observable.zip(observableList, items -> Arrays.toString(items))
                .subscribe(item -> System.out.println(item),
                        error -> System.err.println("Error: " + error),
                        () -> System.out.println("Completed")
        );
    }

    private static void exZipWith() {
        Observable<Integer>ob1=Observable.just(1,2,3,4,5);
        Observable<Integer>ob2=Observable.range(6,5);

        ob1.zipWith(ob2 ,(a,b)->a+b)
                .subscribe(item-> System.out.println(item));
    }

    private static void exZip() {

        Observable<Integer>ob1=Observable.just(1,2,3,4,5);
        Observable<Integer>ob2=Observable.range(6,5);
        Observable<Integer>ob3=Observable.fromIterable(Arrays.asList(11,12,13,14,15));

        Observable.zip(ob1,ob2,ob3 ,(a,b,c)->a+b+c)
                .subscribe(item-> System.out.println(item));

    }

    private static void exmergeInfinite() {
        Observable<String>infinite1=Observable.interval(1,TimeUnit.SECONDS)
                .map(item->"From Infinite1 : "+item);

        Observable<String>infinite2=Observable.interval(2,TimeUnit.SECONDS)
                .map(item->"From Infinite2 : "+item);

        infinite1.mergeWith(infinite2)
                        .subscribe(item-> System.out.println(item));

        pause(7000);

    }

    private static void exMergeWith() {
        Observable<Integer>ob1=Observable.range(1,5);
        Observable<Integer>ob2=Observable.range(6,5);

        ob1.mergeWith(ob2)
                .subscribe(item-> System.out.println(item));
    }

    private static void exMergeItrable() {
        Observable<Integer>ob1=Observable.range(1,5);
        Observable<Integer>ob2=Observable.range(6,5);
        Observable<Integer>ob3=Observable.range(11,5);
        Observable<Integer>ob4=Observable.range(16,5);
        Observable<Integer>ob5=Observable.range(21,5);

        List<Observable<Integer>>observableList=Arrays.asList(ob1,ob2,ob3,ob4,ob5);

        Observable.merge(observableList)
                .subscribe(item-> System.out.println(item));
    }

    private static void exMergeArray() {
        Observable<Integer>ob1=Observable.range(1,5);
        Observable<Integer>ob2=Observable.range(6,5);
        Observable<Integer>ob3=Observable.range(11,5);
        Observable<Integer>ob4=Observable.range(16,5);
        Observable<Integer>ob5=Observable.range(21,5);

        Observable.mergeArray(ob1,ob2,ob3,ob4,ob5)
                .subscribe(item-> System.out.println(item));
    }

    private static void exMerge() {
        Observable<Integer>ob1=Observable.just(1,2,3,4,5);
        Observable<Integer>ob2=Observable.just(6,7,8,9,10);

        Observable.merge(ob1,ob2)
                .subscribe(item-> System.out.println(item));
    }

    private static void DoFinally() {
        Observable.just(1,2,3,4,5)
                .doFinally(()-> System.out.println("DoFinally : Completed"))
                .subscribe(item -> System.out.println(item),
                        error-> System.out.println("Subscribed Error : "+error.getLocalizedMessage()),
                        ()-> System.out.println("Completed"));
    }

    private static void exDoOnComplete() {
        Observable.just(1,2,3,4,5)
                .doOnComplete(() -> System.out.println("DoOnComplete : Complete"))
                .subscribe(item -> System.out.println(item),
                        error-> System.out.println("Subscribed Error : "+error.getLocalizedMessage()),
                        ()-> System.out.println("Completed"));
    }

    private static void exDoOnNext() {
        Observable.just(1,2,3,4,5)
                .doOnNext(item -> System.out.println("DoOnNext : "+item))
                .subscribe(item -> System.out.println(item),
                        error-> System.out.println("Subscribed Error : "+error.getLocalizedMessage()),
                        ()-> System.out.println("Completed"));
    }

    private static void exDoOnSubscribe() {
        Observable.just(1,2,3,4,5)
                .doOnSubscribe(disposable -> System.out.println("DoOnSubscribe : Subscribe"))
                .subscribe(item -> System.out.println(item),
                        error-> System.out.println("Subscribed Error : "+error.getLocalizedMessage()),
                        ()-> System.out.println("Completed"));
    }

    private static void exRetryUntil() {
        AtomicInteger atomicInteger=new AtomicInteger();

        Observable.error(new Exception("This is an example error"))
                .doOnError(error->{
                    System.out.println(atomicInteger.get());
                    atomicInteger.getAndIncrement();
                })
                .retryUntil(()->{
                    System.out.println("Retrying");
                    return atomicInteger.get()>=3;
                })
                .subscribe(item -> System.out.println(item),
                        error-> System.out.println("Subscribed Error : "+error.getLocalizedMessage()),
                        ()-> System.out.println("Completed"));
    }

    private static void exRetry() {
        Observable.error(new Exception("This is an example error"))
                .retry(3)
                .subscribe(item -> System.out.println(item),
                        error-> System.out.println("Subscribed Error : "+error.getLocalizedMessage()),
                        ()-> System.out.println("Completed"));
    }

    private static void retryWithPredicate() {
        Observable.error(new IOException("This is an example error"))
                .retry(error->{
                    if(error instanceof IOException){
                        System.out.println("Retrying");
                        return true;
                    }
                    else{
                        return false;
                    }
                })
                .subscribe(item -> System.out.println(item),
                        error-> System.out.println("Subscribed Error : "+error.getLocalizedMessage()),
                        ()-> System.out.println("Completed"));
    }

    private static void exOnErrorReturnItem() {
        Observable.error(new Exception("This is an example error"))
                .onErrorReturnItem("Hello you got an error!")
                .subscribe(item -> System.out.println(item),
                        error-> System.out.println("Subscribed Error : "+error.getLocalizedMessage()),
                        ()-> System.out.println("Completed"));
    }

    private static void exOnErrorReturn() {
            Observable.error(new IOException("This is an example error"))
                    .onErrorReturn(error->{
                        if(error instanceof IOException){
                            return 1;
                        }
                        else{
                            return 0;
                        }
                    })
                    .subscribe(item -> System.out.println(item),
                            error-> System.out.println("Subscribed Error : "+error.getLocalizedMessage()),
                            ()-> System.out.println("Completed"));

    }

    private static void exOnErrorResumeNext() {
        Observable.error(new Exception("This is an example error"))
                .onErrorResumeNext(throwable -> Observable.just(1,2,3,4,5))
                .subscribe(item -> System.out.println(item),
                        error-> System.out.println("Subscribed Error : "+error.getLocalizedMessage()),
                        ()-> System.out.println("Completed"));
    }

    private static void exDoOnError() {
        Observable.error(new Exception("This is an example error"))
                .doOnError(error-> System.out.println("Error : "+error.getLocalizedMessage()))
                .subscribe(item -> System.out.println(item),
                        error-> System.out.println("Subscribed Error : "+error.getLocalizedMessage()),
                        ()-> System.out.println("Completed"));
    }

    private static void containsWithNonPremetive() {

        User user=new User("Keyush");
        User user2=new User("Keyush");

        Observable.just(user)
                .contains(user2)
                .subscribe(item -> System.out.println(item));
    }

    static class User{
        private String name;
        User(String name){
            this.name=name;
        }
    }

    private static void containsWithPremetive() {
        Observable.just(1, 2, 3, 4, 5)
                .contains(7)
                .subscribe(item -> System.out.println(item));
    }

    private static void delayError() {
        Observable.error(new Exception("Error"))
                .delay(3,TimeUnit.SECONDS,true)
                .subscribe(item -> System.out.println(item),
                        error-> System.out.println("Error : "+error.getLocalizedMessage()),
                        ()-> System.out.println("Completed!"));

        pause(5000);
    }

    private static void useDelay() {
        Observable.just(1, 2, 3, 4, 5)
                .delay(3000,TimeUnit.MILLISECONDS)
                .subscribe(item -> System.out.println(item));

        pause(7000);
    }

    private static void useSortedOnNOnComparator() {
        Observable.just("app","catle","egg","ball")
                .sorted((first,second)->Integer.compare(first.length(),second.length()))
                .subscribe(item -> System.out.println(item));
    }

    private static void useSortedWithOwnOperator() {
        Observable.just(3, 4,1,2, 5)
                .sorted(Comparator.reverseOrder())
                .subscribe(item -> System.out.println(item));
    }

    private static void useSorted() {
        Observable.just(3, 4,1,2, 5)
                .sorted()
                .subscribe(item -> System.out.println(item));
    }

    private static void useScanWithInitialValue() {

        Observable.just(1, 2, 3, 4, 5)
                .scan(10,(accumulator, next) -> accumulator + next)
                .subscribe(item -> System.out.println(item));

    }

    private static void useScan() {
        Observable.just(1,2,3,4,5)
                .scan((accumulator, next) -> accumulator + next)
                .subscribe(item-> System.out.println(item));
    }

    private static void useRepeat() {
        Observable.just(1,2,3,4,5)
                .repeat(2)
                .subscribe(item-> System.out.println(item));
    }

    private static void useSwitchIfEmpty() {
        Observable.just(1,2,3,4,5)
                .filter(item->item>7)
                .switchIfEmpty(Observable.just(6,7,8,9))
                .subscribe(item-> System.out.println(item));
    }

    private static void useDefaultIfEmpty() {
        Observable.just(1,2,3,4,5)
                .filter(item->item>7)
                .defaultIfEmpty(100)
                .subscribe(item-> System.out.println(item));
    }

    private static void distinctUntilChangeWithKeyOperator() {
        Observable.just("foo","fool","fail","fruit","feet","farm","fan")
                .distinctUntilChanged(item->item.length())
                .subscribe(item-> System.out.println(item));
    }

    private static void distinctUntilChangeOperator() {
        Observable.just(1,1,2,2,3,3,4,1,2,3)
                .distinctUntilChanged()
                .subscribe(item-> System.out.println(item));
    }

    private static void distinctWithKeySelector() {
        Observable.just("foo","fool","fail","fruit")
                .distinct(item->item.length())
                .subscribe(item-> System.out.println(item));
    }

    private static void distinctOperator() {
        Observable.just(1,2,3,4,5,1,2,3,7)
                .distinct()
                .subscribe(item-> System.out.println(item));
    }

    private static void skipWhileOperator() {
        Observable.just(1,2,3,4,5,1,2,3,4,5)
                .skipWhile(item->item<=3)
                .subscribe(item-> System.out.println(item));
    }

    private static void skipOperator() {
        Observable.just(1,2,3,4,5)
                .skip(2)
                .subscribe(item-> System.out.println(item));
    }

    private static void takeWhileOperator() {
        Observable.just(1,2,3,4,5,1,2,3,4,5)
                .takeWhile(item->item<=3)
                .subscribe(item-> System.out.println(item));
    }

    private static void takeWithInterval() {
        Observable.interval(300,TimeUnit.MILLISECONDS)
                .take(2,TimeUnit.SECONDS)
                .subscribe(item-> System.out.println(item), System.out::println,()-> System.out.println("Completed!"));

        pause(5000);
    }

    private static void takeOperator() {
        Observable<Integer>observable=Observable.just(3,4,5);

        observable
                .take(2)
                .subscribe(item-> System.out.println(item));
    }

    private static void combineMapAndFilterOpeartor() {
        Observable<Integer>observable=Observable.just(1,2,3,4,5);

        observable
                .filter(item->item%2==0)
                .map(item->item*2)
                .subscribe(item-> System.out.println(item));
    }

    private static void filterOperator() {
        Observable<Integer>observable=Observable.just(1,2,3,4,5);

        observable
                .filter(item->item%2==0)
                .subscribe(item-> System.out.println(item));
    }

    private static void mapOperator() {
        Observable<Integer>observable=Observable.just(1,2,3,4,5);

        observable
                .map(item->item*2)
                .subscribe(item-> System.out.println(item));
    }

    private static void compositeDisposable() {
        CompositeDisposable compositeDisposable=new CompositeDisposable();
        Observable<Long>observable=Observable.interval(1,TimeUnit.SECONDS);

        Disposable disposable1=  observable.subscribe(item-> System.out.println("Observable 1: "+item));
        Disposable disposable2=  observable.subscribe(item-> System.out.println("Observable 2: "+item));

        compositeDisposable.addAll(disposable1,disposable2);

        pause(3000);
        compositeDisposable.delete(disposable1);
        compositeDisposable.dispose();
        pause(5000);
    }

    private static void handleDisposableOutsideObserver() {
        Observable<Integer>observable=Observable.just(1,2,3,4,5);

        ResourceObserver<Integer> observer=new ResourceObserver<Integer>() {

            @Override
            public void onNext(@NonNull Integer integer) {

                System.out.println(integer);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        Disposable d=observable.subscribeWith(observer);
    }

    private static void handleDisposableInObserver() {
        Observable<Integer>observable=Observable.just(1,2,3,4,5);

        Observer<Integer>observer=new Observer<Integer>() {

            Disposable disposable;

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable=d;
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                if(integer==3){
                    disposable.dispose();
                }

                System.out.println(integer);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        observable.subscribe(observer);
    }

    private static void handleDisposable() {
        Observable<Long>observable=Observable.interval(1,TimeUnit.SECONDS);

        Disposable disposable=observable.subscribe(item-> System.out.println(item));
        pause(3000);

        disposable.dispose();
        pause(3000);
    }

    private static void createCompletable() {
        Completable.fromSingle(Single.just("Hello World")).subscribe(()-> System.out.println("Done!"));
    }

    private static void createMayBe() {
        Maybe.empty().subscribe(new MaybeObserver<Object>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull Object o) {
                System.out.println(o);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                System.out.println("Done");
            }
        });
    }

    private static void createSingle() {
        Single.just("Hello World")
                .subscribe(System.out::println);
    }

    private static void pause(int i) {
        try {
            Thread.sleep(i);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private static int getNumber(){
        System.out.println("Generating");
        return 1/0;
    }

    private static void createObservableUsingEmpty() {
        Observable observable=Observable.empty();

        observable.subscribe(System.out::println, System.out::println, ()-> System.out.println("Completed"));
    }

    private static void createObservableUsingNever() {
        Observable observable=Observable.never();

        observable.subscribe(System.out::println, System.out::println, ()-> System.out.println("Completed"));

    }

    private static void throwExceptionUsingCallable() {
        Observable observable=Observable.error(() -> new Exception("An Exception Occured"));

        observable.subscribe(System.out::println, error-> System.out.println("Observer 1 error : "+error.hashCode()));

        observable.subscribe(System.out::println, error-> System.out.println("Observer 2 error : "+error.hashCode()));
    }

    private static void throwException() {
        Observable observable=Observable.error(new Exception("An Exception Occured"));

        observable.subscribe(System.out::println, error-> System.out.println("Observer 1 error : "+error.hashCode()));

        observable.subscribe(System.out::println, error-> System.out.println("Observer 2 error : "+error.hashCode()));
    }

    private static void createHotAndColdConnectableObservable() {
        ConnectableObservable<Integer>observable=Observable.just(1,2,3,4,5).publish();

        observable.subscribe(item-> System.out.println("Observable 1 :"+item));

        observable.connect();

        observable.subscribe(item-> System.out.println("Observable 2 :"+item));
    }

    private static void createColdObservable() {
        Observable<Integer>observable=Observable.just(1,2,3,4,5);


        observable.subscribe(item-> System.out.println("Observer 1 : " + item));

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        observable.subscribe(item-> System.out.println("Observer 2 : " + item));
    }

    private static void CreateObserVableWithJust() {
        Observable<Integer> observable=Observable.just(1,2,3,4,5);

        observable.subscribe(item-> System.out.println(item));
    }

    private static void CreateObserVableFromIterable() {
        List<Integer>list= Arrays.asList(1,2,3,4);

        Observable<Integer>observable=Observable.fromIterable(list);

        observable.subscribe(item-> System.out.println(item));
    }

    private static void CreateObserVableUsingCreate() {

        Observable<Integer>observable=Observable.create(emitter -> {
            emitter.onNext(1);
            emitter.onNext(2);
            emitter.onNext(3);
            emitter.onNext(4);
            emitter.onNext(5);
            emitter.onNext(null);
            emitter.onComplete();

        });

        observable.subscribe(item-> System.out.println(item),
                error-> System.out.println("Error : "+error.getLocalizedMessage()),
                ()-> System.out.println("Completed"));
    }
}