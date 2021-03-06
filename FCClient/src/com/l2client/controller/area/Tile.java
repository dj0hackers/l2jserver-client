package com.l2client.controller.area;

import java.util.logging.Logger;

import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.TangentBinormalGenerator;
import com.l2client.app.Singleton;
import com.l2client.asset.Asset;
import com.l2client.controller.SceneManager.Action;
import com.l2client.model.l2j.ServerValues;
import com.l2client.navigation.TiledNavMesh;
import com.l2client.util.GrassLayerUtil;

public class Tile {
	private static Logger log = Logger.getLogger(Tile.class.getName());
	static boolean moveNonNavToOrigin = false;
	static final String loadPath = "/tile/";
	volatile String areaPath = "";
	volatile int x,z;
	volatile Asset base = null;
	volatile Asset detail1 = null;
	volatile Asset nav = null;
	int hash =0;
	
	public Tile(int _x, int _z) {
		if(_x <0 || _x>9999|| _z <0 || _z>9999)
			throw new RuntimeException("Tile parameters outside 0-9999 range: ("+_x+"/"+_z+"), trying too load too small tiles?");
		x = _x;
		z = _z;		
		areaPath = loadPath+x+"_"+z+"/";
	}
	
    public boolean equals(Object obj) {
		if (obj instanceof Tile) {
			Tile pt = (Tile) obj;
			return (x == pt.x) && (z == pt.z);
		}
		return super.equals(obj);
	}

    //HashCode build by 1 xxxx yyyy as a number, x and y should be no bigger than 9999
	public int hashCode() {
		if(hash != 0)
			return hash;
		else {
			int n = 100000000;
			n = n + x*10000;
			n = n + z;
			hash = n;
			return hash;
		}
	}
	
    public static void main(String[] args){
       System.out.println("world x=0 should be tile 160:"+getTileFromWorldXPosition(0));
       System.out.println("world x=256 should be tile 161:"+getTileFromWorldXPosition(256));
       System.out.println("world x=-256 should be tile 161:"+getTileFromWorldXPosition(-256));
       System.out.println("world x=2048 should be tile 168:"+getTileFromWorldXPosition(2048));
       System.out.println("world x=-2048 should be tile 152:"+getTileFromWorldXPosition(-2048));
       System.out.println("world z=0 should be tile 144:"+getTileFromWorldZPosition(0));
       System.out.println("world z=256 should be tile 145:"+getTileFromWorldZPosition(256));
       System.out.println("world z=-256 should be tile 143:"+getTileFromWorldZPosition(-256));
       System.out.println("world z=2048 should be tile 152:"+getTileFromWorldZPosition(2048));
       System.out.println("world z=-2048 should be tile 136:"+getTileFromWorldZPosition(-2048));
       System.out.println("tile x=0 should be world -160*2048:"+getWorldPositionOfXTile(0));
       System.out.println("tile x=160 should be world 0:"+getWorldPositionOfXTile(160));
       System.out.println("tile x=161 should be world 256:"+getWorldPositionOfXTile(161));
       System.out.println("tile x=159 should be world -256:"+getWorldPositionOfXTile(159));
       System.out.println("tile x=168 should be world 2048:"+getWorldPositionOfXTile(168));
       System.out.println("tile x=152 should be world -2048:"+getWorldPositionOfXTile(152));
       System.out.println("tile z=0 should be world -144*2048:"+getWorldPositionOfZTile(0));
       System.out.println("tile z=144 should be world 0:"+getWorldPositionOfZTile(144));
       System.out.println("tile z=145 should be world 256:"+getWorldPositionOfZTile(145));
       System.out.println("tile z=143 should be world -256:"+getWorldPositionOfZTile(143));
       System.out.println("tile z=152 should be world 2048:"+getWorldPositionOfZTile(152));
       System.out.println("tile z=136 should be world -2048:"+getWorldPositionOfZTile(136));
       System.out.println("tile x=121 should be world -9984:"+getWorldPositionOfXTile(121));
       System.out.println("tile x=122 should be world -9728:"+getWorldPositionOfXTile(122));
       System.out.println("tile z=176 should be world 8192:"+getWorldPositionOfZTile(176));
       System.out.println("tile z=177 should be world 8448:"+getWorldPositionOfZTile(177));
       System.out.println("tile x=143 should be world -4352:"+getWorldPositionOfXTile(143));
       System.out.println("tile z=207 should be world 16128:"+getWorldPositionOfZTile(207));
       System.out.println("Client:"+ServerValues.getClientCoordX(-72064)+"/"+ServerValues.getClientCoordZ(257664)+
    		   " Tile:"+getTileFromWorldXPosition((int) ServerValues.getClientCoordX(-72064))+"/"+
    		   getTileFromWorldZPosition((int) ServerValues.getClientCoordZ(257664)));
       System.out.println("Client:"+ServerValues.getClientCoordX(-71536)+"/"+ServerValues.getClientCoordZ(258752)+
    		   " Tile:"+getTileFromWorldXPosition((int) ServerValues.getClientCoordX(-71536))+"/"+
    		   getTileFromWorldZPosition((int) ServerValues.getClientCoordZ(258752)));
       System.out.println("tile x=121 should be world -9984:"+getWorldPositionOfXTile(121));
       System.out.println("tile z=176 should be world 8192:"+getWorldPositionOfZTile(176));
       System.out.println("Client:"+ServerValues.getClientCoordX(-9984)+"/"+ServerValues.getClientCoordZ(8192)+
    		   " Tile:"+getTileFromWorldXPosition((int) ServerValues.getClientCoordX(-9984))+"/"+
    		   getTileFromWorldZPosition((int) ServerValues.getClientCoordZ(8192)));
    }
	
	/** 
	 * l2j's center is in x between region 19 (-32768) and 20 (+32768)
	 * @param x
	 * @return
	 */
	public static int getTileFromWorldXPosition(int x){
//		int region = x>>5;// 32x32 regions
//		int pos = region>>3; // with 8x8 tiles of 256x256 world size
		return (x>>8)+160;// +160 (20*8 tiles) because 0 in x is in tile 160 not in tile 0
	}
	
	/**  
	 * and in y between region 18 (+32768) and 17 (-32768)
	 * 
	 * @param z
	 * @return
	 */
	public static int getTileFromWorldZPosition(int z){
//		int region = x>>3;// =x/8
//		int pos = region>>8; // =region/256
		return (z>>8)+144; //+144 because 0 in z is in tile 144 not in tile 0		
	}
	
	public static int getWorldPositionOfXTile(int xTile){
		return (xTile-160)<<8;
	}
	
	public static int getWorldPositionOfZTile(int zTile){
		return (zTile-144)<<8;
	}
	
//	public static int getXDistanceFromTile(int xWorldPos, int tile){
//		//the center of the tile is in the topleft corner
//		//move left
//		int tileCenter = getWorldPositionOfXTile(tile)-IArea.TERRAIN_SIZE_HALF;
//		return xWorldPos-tileCenter;
//	}
//	
//	public static int getZDistanceFromTile(int zWorldPos, int tile){
//		//the center of the tile is in the topleft corner
//		//move up
//		int tileCenter = getWorldPositionOfZTile(tile)+IArea.TERRAIN_SIZE_HALF;
//		return zWorldPos-tileCenter;
//	}
	
	class DeferredTerrainAsset extends Asset{
		public DeferredTerrainAsset(String loc){
			super(loc, loc);
		}
		@Override
		public void afterLoad(){
			if(baseAsset != null && baseAsset instanceof Spatial){
				log.finer("DeferredTerrainAsset afterLoad of:"+this.name);
				Spatial n = (Spatial)baseAsset;
			    if(moveNonNavToOrigin)
			    	n.setLocalTranslation(Tile.getWorldPositionOfXTile(x), 0f,Tile.getWorldPositionOfZTile(z));

////this is just for demonstration, move this out, should be detail1 or grass1
//log.finer("TerrainTile loaded at:"+n.getWorldTranslation());
//
//if(!(n instanceof Node)){
//	n = new Node(n.getName());
//	((Node)n).attachChild((Spatial) baseAsset);
//	baseAsset = n;
//}
//for(Spatial s: ((Node)n).getChildren()){
//	
//	if(s.getName().startsWith(IArea.TILE_PREFIX)){
//		Node c = GrassLayerUtil.createPatchField(s, Singleton.get().getAssetManager().getJmeAssetMan(), 
////				"/vegetation/grass/grass/g3.tga", 1.2f, 2.5f, 1f, 1f, 400f, 100f, 0.6f,0,1);
//				"/vegetation/grass/grass_scattered/gs.tga", 1.2f, 4f, 1f, 1.5f, 200f, 75f, 0.6f,0,1);
//		((Node) n).attachChild(c);	
//	}
//}
			    //make them all shadow casters and receivers
			    if(getLocation().endsWith("base.j3o")){
			    	setShadowCasterReceiver(n);
			    }
			    
				Singleton.get().getSceneManager().changeTerrainNode(n,Action.ADD);
			}
		}
		
		private void setShadowCasterReceiver(Spatial n) {
			if(n instanceof Geometry) {
				Geometry v = (Geometry)n;
				if(n.getQueueBucket().equals(RenderQueue.Bucket.Opaque))
					n.setShadowMode(ShadowMode.CastAndReceive);
//					TangentBinormalGenerator.generate(n);
			} else if (n instanceof Node){
				Node v = (Node)n;
				for(Spatial  child : v.getChildren()){
					setShadowCasterReceiver(child);
				}
			}
		}
		
		@Override
		public void beforeUnload(){
			if(baseAsset != null && baseAsset instanceof Spatial){
				log.finer("DeferredTerrainAsset beforeUnload of:"+this.name);
				Singleton.get().getSceneManager().changeTerrainNode((Spatial)baseAsset,Action.REMOVE);
				baseAsset = null;
			}
		}
	}
	
	class NavAsset extends Asset{
		public NavAsset(String loc){
			super(loc, loc);
		}
		@Override
		public void afterLoad(){
			if(baseAsset != null && baseAsset instanceof TiledNavMesh ){
				if(moveNonNavToOrigin){
					TiledNavMesh  n = (TiledNavMesh)baseAsset;
					// center is in top left corner for nav mesh to be consistent with
					// this for borders move x right, move y up by half size
					//Vector3f offset = new Vector3f(xd + 128, 0, yd - 128);
					//n.setPosition(new Vector3f(Tile.getWorldPositionOfXTile(x)+IArea.TERRAIN_SIZE_HALF, 0f,Tile.getWorldPositionOfZTile(z)-IArea.TERRAIN_SIZE_HALF));
					n.setPosition(new Vector3f(Tile.getWorldPositionOfXTile(x), 0f,Tile.getWorldPositionOfZTile(z)));
//					System.out.println(getLocation()+" Navmesh "+n+" moved to:"+n.getPosition());
					log.fine("Navmesh "+n+" moved to:"+n.getPosition());
				}
				Singleton.get().getNavManager().attachMesh((TiledNavMesh) baseAsset);
			}
			log.finer("NavAsset afterLoad of:"+this.name);
		}
		
		@Override
		public void beforeUnload(){
			if(baseAsset != null && baseAsset instanceof TiledNavMesh ){
				Singleton.get().getNavManager().detachMesh((TiledNavMesh) baseAsset);
				baseAsset = null;
				log.finer("NavAsset beforeUnload of:"+this.name);
			}
		}
	}
	
	void load(boolean prioLoadNav){
//		nav = new NavAsset(areaPath+x+"_"+z+".jnv");
		nav = new NavAsset(areaPath+"nav.jnv");
		Singleton.get().getAssetManager().loadAsset(nav, prioLoadNav);
//		base = new DeferredTerrainAsset(areaPath+x+"_"+z+".j3o");
		base = new DeferredTerrainAsset(areaPath+"base.j3o");
		Singleton.get().getAssetManager().loadAsset(base, false);
		//TODO rework detail loading, moving to correct offest, etc..
//		detail1 = new DeferredTerrainAsset(areaPath+"detail.j3o");
//		Singleton.get().getAssetManager().loadAsset(detail1, false);
		
		/*
			Quad q = new Quad(1f * TERRAIN_SIZE, 1f * TERRAIN_SIZE);
			Geometry n = new Geometry(x + " " + y,q);
			n.setMaterial(material);
			n.setLocalTranslation(x * TERRAIN_SIZE, 0f,y * TERRAIN_SIZE);
			ret.patch = n;
			n.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
			Singleton.get().getSceneManager().changeTerrainNode(n,0);
		 * */
	}

	void unLoad(){
		if(nav != null){
			nav.beforeUnload();
			nav = null;
		}
		if(base != null){
			base.beforeUnload();
			base = null;
		}
		if(detail1 != null){
			detail1.beforeUnload();
			detail1 = null;
		}
	}
}
