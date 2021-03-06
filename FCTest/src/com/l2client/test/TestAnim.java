package com.l2client.test;

import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.l2client.app.ExtendedApplication;
import com.l2client.app.Singleton;
import com.l2client.component.AnimationSystem;
import com.l2client.component.EnvironmentComponent;
import com.l2client.component.VisualComponent;
import com.l2client.controller.entity.Entity;
import com.l2client.controller.entity.EntityManager;
import com.l2client.model.PartSet;
import com.l2client.model.jme.ItemModel;
import com.l2client.model.jme.NPCModel;
import com.l2client.model.jme.VisibleModel;
import com.l2client.model.l2j.ItemInstance;
import com.l2client.model.network.NewCharSummary;
import com.l2client.util.PartSetManager;

public class TestAnim extends ExtendedApplication {
	
	private static final int _1000 = 1000;

	PartSetManager man;
	
	
	static float time = 0f;
	static int step = 0;
	
	private PartSet con;

	private AnimationSystem as;

	private EntityManager em;

	private Entity ent;
	
	private VisibleModel mod;

    public void simpleInitApp() {
    	
    	Singleton.get().init(null);
    	man = Singleton.get().getPartManager();
    	
    	man.loadParts("megaset.csv");
         
        //move cam a bit closer
//        cam.setLocation(cam.getLocation().mult(-1f));
    	cam.lookAt(new Vector3f(0,2f,0), Vector3f.UNIT_Y);
        inputManager.setCursorVisible(true);
        flyCam.setEnabled(false);
        
        DirectionalLight dr = new DirectionalLight();
        dr.setColor(ColorRGBA.White);
        dr.setDirection(new Vector3f(1, 0 , 1));
        
        AmbientLight am = new AmbientLight();
        am.setColor(ColorRGBA.White);
        rootNode.addLight(am);
        rootNode.addLight(dr);

        setupGUI();
        setupScene();

    }
 
    private void setupGUI() {
    	em = Singleton.get().getEntityManager();
    	as = Singleton.get().getAnimSystem();
	}

    private void setupScene() {

    	//TODO could use getTemplates to switch through models..
    	NewCharSummary sum = new NewCharSummary();
    	sum.name = "DwarfWarriorM";
    	NPCModel m = new NPCModel(sum);
    	//Node n = Assembler2.getModel3("dwarfwarrior"); //"pelffwarrior");//humanhalberd"); //goblin");
    	m.attachVisuals();
//    	if(n != null){	
    		ent = em.createEntity(_1000);
    		
    		Node nn = new Node("intermed");
    		nn.attachChild(m);
    		ent.attachChild(nn);
    		
    		rootNode.attachChild(ent);
    		VisualComponent vis = new VisualComponent();
    		EnvironmentComponent env = new EnvironmentComponent();
    		
    		em.addComponent(ent.getId(), env);
    		em.addComponent(ent.getId(), vis);
    		
    		vis.vis = m;
    		mod = m;

    		as.addComponentForUpdate(env);
//    	}
    }

    public void simpleUpdate(float tpf) {
    	time += tpf;
    	
    	if(time > 7.5f && time < 8.0f && step == 0){
    		do5Update();
    		mod.addMessageLabel("+12", ColorRGBA.Red, 2f, 1f);
    		step++;
    	}
    	else if (time > 20f && time < 20.5f && step == 1){
    		do9Update();
    		step++;
    	}else if (time > 30f && time < 30.3f && step == 2){
    		do12Update();
    		step++;
    	}else if (time > 40f && time < 40.6f && step == 3){
    		do20Update();
    		step++;
    	}else if (time > 60f && time < 60.11f && step == 4){
    		do31Update();
    		step++;
    	}else if (time > 70f){
    		do60Update();
    		step++;
    	}
    	as.update(tpf);
    }
    
	private void do5Update() {
		if(step != 0)
			return;
		EnvironmentComponent env = (EnvironmentComponent) EntityManager
				.get().getComponent(_1000, EnvironmentComponent.class);
		if (env != null) {
			env.movement = 0;
			env.changed = true;
System.out.println(time+" SHOULD walk");
		}
		step++;
	}
	
	private void do9Update() {
		if(step != 1)
			return;
		step++;
		EnvironmentComponent env = (EnvironmentComponent) EntityManager
				.get().getComponent(_1000, EnvironmentComponent.class);
		if (env != null) {
			env.movement = -1;
			env.changed = true;
System.out.println(time+" SHOULD STOOOOOOOOPPPPP");
		}
	}

	private void do12Update() {
		if(step != 2)
			return;
		step++;
		EnvironmentComponent env = (EnvironmentComponent) EntityManager
				.get().getComponent(_1000, EnvironmentComponent.class);
		if (env != null) {
			env.movement = 1;
			env.changed = true;
System.out.println(time+" SHOULD ruuuuunn");
		}
	}
	
	private void do20Update() {
		if(step != 3)
			return;
		step++;
		EnvironmentComponent env = (EnvironmentComponent) EntityManager
				.get().getComponent(_1000, EnvironmentComponent.class);
		if (env != null) {
			env.movement = -1;
			env.changed = true;
System.out.println(time+" SHOULD STOOOOOOOOPPPPP");
		}
	}
	
	private void do31Update() {
		if(step != 4)
			return;
		step++;
		EnvironmentComponent env = (EnvironmentComponent) EntityManager
				.get().getComponent(_1000, EnvironmentComponent.class);
		if (env != null) {
			env.movement = -1;
			env.changed = true;
System.out.println(time+" SHOULD STOOOOOOOOPPPPP");
		}
	}
	
	private void do60Update() {
		if(step != 5)
			return;
		step++;
		EnvironmentComponent env = (EnvironmentComponent) EntityManager
				.get().getComponent(_1000, EnvironmentComponent.class);
		if (env != null) {
			env.movement = -1;
			env.changed = true;
System.out.println(time+" SHOULD STOOOOOOOOPPPPP");
		}
	}
	
	/**
     * Entry point
     */
    public static void main(String[] args) {
    	TestAnim app = new TestAnim();
        app.start();
    }
   
}
