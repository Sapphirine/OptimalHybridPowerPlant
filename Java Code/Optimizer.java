import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Optimizer{

	public static double ld=100000;
	public static double epv=15;
	public static double ewtg=12;
	public static double cpv=300;
	public static double cwtg=3500;
	
	public static double epThresh=0.9; 
	public static double demandThresh=0.5;
	
	public static final String NO_MINIMA="NO MINIMAL FOUND";
	public static final String UNIVERSAL_KEY="u";
	private static final String COMMA_DELIMITER = ",";
	private static final String UNFIT = "unfit";
	private static final String OPTIMAL = "OPTIMAL>>>";
	
	public static class ValueMapper extends Mapper<Object, Text, Text, Text> {

		public String calc(int npv, int nwtg){
			double ep, cost, lr;
			StringBuilder result=new StringBuilder();
			result.append(npv).append(COMMA_DELIMITER).append(nwtg).append(COMMA_DELIMITER);
			cost=cpv*npv+cwtg*nwtg;
			ep=((epv*npv+ewtg*nwtg)-ld)/ld;
			lr=(epv*npv+ewtg*nwtg)/ld;
			if(ep<0)ep=0;
			if (ep>epThresh || lr<demandThresh){
				return UNFIT;	
			}else{
				result.append(cost).append(COMMA_DELIMITER)
				.append(ep).append(COMMA_DELIMITER)
				.append(lr);
				return result.toString();
			}
			
		}
		
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			
			String[] inputs = value.toString().split(COMMA_DELIMITER);
			String result;
			result=calc(Integer.parseInt(inputs[0]), Integer.parseInt(inputs[1]));
			Text intval= new Text();
			Text intkey= new Text();
			if (!result.equals(UNFIT)){
			intval.set(result);
			intkey.set(UNIVERSAL_KEY);
			context.write(intkey, intval);
			}
		}
		
		
	}
	
	
	public static class CostReducer extends Reducer<Text, Text, Text, Text> {
		private Text result;

		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			String[] valSplitter; 
			double minCost = 0;
			
			for (Text val : values) {
				valSplitter=val.toString().split(COMMA_DELIMITER);
				Double cost= Double.parseDouble(valSplitter[2]);
				
				//initializing the minimum value
				if(minCost==0)minCost=cost;
				//optimizing for minimum cost, constraints have been taken care of in mapping
				if (cost <=minCost){
					minCost=cost;
					result=val;
				}
				
			}
			if (null==result){
				result=new Text(NO_MINIMA);
			}else{
				result=new Text(OPTIMAL+result.toString());
			}
			//result.set(sum);
			context.write(key, result);
		}
	}
	
	
	
	
	public static void main(String[] args) {
	    Job job;
		Configuration conf = new Configuration();

		try {
			job = Job.getInstance(conf, "optimizer");
		    job.setJarByClass(Optimizer.class);
		    job.setMapperClass(ValueMapper.class);
		    job.setCombinerClass(CostReducer.class);
		    job.setReducerClass(CostReducer.class);
		    job.setOutputKeyClass(Text.class);
		    job.setOutputValueClass(Text.class);
		    FileInputFormat.addInputPath(job, new Path(args[0]));
		    FileOutputFormat.setOutputPath(job, new Path(args[1]));
		    System.exit(job.waitForCompletion(true) ? 0 : 1);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	    
	}

}


