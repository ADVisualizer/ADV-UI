package ch.adv.ui.core.logic;

import org.mockito.Mockito;

public class TestModule implements ADVModule {

    private final Layouter layouter = Mockito.mock(Layouter.class);
    private final Stringifyer stringifyer = Mockito.mock(Stringifyer.class);
    private final Parser parser = Mockito.mock(Parser.class);

    @Override
    public Layouter getLayouter() {
        return layouter;
    }

    @Override
    public Stringifyer getStringifyer() {
        return stringifyer;
    }

    @Override
    public Parser getParser() {
        return parser;
    }
}
