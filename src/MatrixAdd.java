package ssafy;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class MatrixAdd {
  public static class MAddMapper  extends  Mapper<Object, Text, Text , IntWritable>{

     public void map(Object key, Text value, Context context)
	throws IOException, InterruptedException {
  

 	String[] arr = value.toString().split("\t"); // [A|B] row_num col_num value
 	
 	Text coordi = new Text(arr[1] + "\t" + arr[2]);
 	IntWritable intWritable = new IntWritable(Integer.parseInt(arr[3]));
	context.write(coordi, intWritable);

     }
  }
  
  public static class  MAddReducer  extends Reducer<Text, IntWritable, Text, IntWritable> {
  
  
    // variables
    private IntWritable result = new IntWritable();
    public void reduce(Text  key, Iterable<IntWritable> values, Context  context) 
	throws IOException, InterruptedException {


	int sum = 0;
	for ( IntWritable val : values ) {
		sum += val.get();
	}
	result.set(sum);
	context.write(key,result);
	

    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
    if (otherArgs.length != 2) {	
      System.err.println("Usage: <in> <out>");
      System.exit(2);
    }
    FileSystem hdfs = FileSystem.get(conf);
    Path output = new Path(otherArgs[1]);
    if (hdfs.exists(output))
            hdfs.delete(output, true);

    Job job = new Job(conf, "matrix addition");
    job.setJarByClass(MatrixAdd.class);		
    job.setMapperClass(MAddMapper.class);	            
    job.setReducerClass(MAddReducer.class);	       
    job.setOutputKeyClass(Text.class);		
    job.setOutputValueClass(IntWritable.class);
    job.setNumReduceTasks(2);		

    FileInputFormat.addInputPath(job, new Path(otherArgs[0]));	
    FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));	
    System.exit(job.waitForCompletion(true) ? 0 : 1);	
  }
}
