package game.server.core;

import game.server.Solve;
import game.server.entity.Result;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by tranq on 6/3/2017.
 */
public class SolveTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    /**
     * Login: Username-tri
     * */
    @Test
    public void login() throws Exception {
        Solve solve = new Solve();
        Result ret = solve.solvingForServer(0, 0, "Username-tri");
        Assert.assertEquals(ret.getMessage(),"tri" + " " + 0 + " " + 0 + "-should click 'ACCEPT' to start game");
    }
}