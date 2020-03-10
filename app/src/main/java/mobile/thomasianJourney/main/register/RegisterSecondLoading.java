package mobile.thomasianJourney.main.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

import mobile.thomasianJourney.main.interfaces.AsyncResponse;
import mobile.thomasianJourney.main.register.async.RegisterSecondAsync;
import mobile.thomasianJourney.main.register.utils.IntentExtrasAddresses;
import mobile.thomasianJourney.main.vieweventsfragments.R;

public class RegisterSecondLoading extends AppCompatActivity {

	private String mEmail, mMobile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_second_loading_layout);

		initializeViews();

		Intent intent = getIntent();

		if (intent != null) {
			mEmail = intent.getStringExtra(IntentExtrasAddresses.INTENT_EXTRA_EMAIL_ADDRESS);
			mMobile = intent.getStringExtra(IntentExtrasAddresses.INTENT_EXTRA_MOBILE_NUMBER);
			int studentsId = intent.getIntExtra(IntentExtrasAddresses.INTENT_EXTRA_STUDENTS_ID, -1);
			String verificationCode = intent.getStringExtra(IntentExtrasAddresses.INTENT_EXTRA_VERIFICATION_CODE);

			AsyncResponse asyncResponse = new AsyncResponse() {
				@Override
				public void doWhenFinished(String output) {
					verifyCredentials(output);
				}
			};

			RegisterSecondAsync registerSecondAsync = new RegisterSecondAsync(asyncResponse);

			registerSecondAsync.execute(verificationCode, String.valueOf(studentsId), getString(R.string.registerCheckCodeURL));
		}
	}

	private void initializeViews() {
		HorizontalStepView stepview = findViewById(R.id.registerSecondLoading_stepView);

		List<StepBean> stepsBeanList = new ArrayList<>();
		StepBean stepBean0 = new StepBean("", 1);
		StepBean stepBean1 = new StepBean("", 1);
		StepBean stepBean2 = new StepBean("", 0);
		stepsBeanList.add(stepBean0);
		stepsBeanList.add(stepBean1);
		stepsBeanList.add(stepBean2);

		stepview.setStepViewTexts(stepsBeanList).setTextSize(12)//set textSize
				.setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(RegisterSecondLoading.this, android.R.color.black)).setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(RegisterSecondLoading.this, R.color.uncompleted_text_color)).setStepViewComplectedTextColor(ContextCompat.getColor(RegisterSecondLoading.this, android.R.color.black)).setStepViewUnComplectedTextColor(ContextCompat.getColor(RegisterSecondLoading.this, R.color.uncompleted_text_color)).setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(RegisterSecondLoading.this, R.drawable.ic_check_black)).setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(RegisterSecondLoading.this, R.drawable.ic_radio)).setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(RegisterSecondLoading.this, R.drawable.tiger_rar));

		LottieAnimationView registerSecondLoading_lottieAnimationView = findViewById(R.id.registerSecondLoading_lottieAnimationView);
		registerSecondLoading_lottieAnimationView.setScale(7f);
		registerSecondLoading_lottieAnimationView.setVisibility(View.VISIBLE);
		registerSecondLoading_lottieAnimationView.setAnimation(R.raw.load);
		registerSecondLoading_lottieAnimationView.playAnimation();

		Animation myanim = AnimationUtils.loadAnimation(this, R.anim.mytransition);

		TextView registerSecondLoading_titleTextView = findViewById(R.id.registerSecondLoading_titleTextView);
		TextView registerSecondLoading_subtitleTextView = findViewById(R.id.registerSecondLoading_subtitleTextView);

		registerSecondLoading_titleTextView.startAnimation(myanim);
		registerSecondLoading_subtitleTextView.startAnimation(myanim);
	}

	public void verifyCredentials(String s) {

		if (!TextUtils.isEmpty(s)) {
			Gson gson = new Gson();
			JsonObject jsonObject;

			try {
				jsonObject = gson.fromJson(s, JsonObject.class);

				if (jsonObject != null && jsonObject.has("data")) {

					JsonObject dataObject = jsonObject.get("data").getAsJsonObject();

					if (dataObject != null) {

						String emailAddressToPass;
						String mobileNumberToPass;
						String studentsIdToPass;
						String verificationCodeToPass;
						String studentEmail, studentName, studentCollegeId, studentYearLevelId;
						int studentPoints;
						JsonObject studentJsonObject;

						if (dataObject.get("studregEmail") != null) {
							emailAddressToPass = dataObject.get("studregEmail").getAsString();

							if (dataObject.get("studregmobileNum") != null) {
								mobileNumberToPass =
										dataObject.get("studregmobileNum").getAsString();

								if (dataObject.get("studentsId") != null) {
									studentsIdToPass = dataObject.get("studentsId").getAsString();

									if (dataObject.get("numbercode") != null) {
										verificationCodeToPass =
												dataObject.get("numbercode").getAsString();

										if (dataObject.get("studentDetails") != null) {
											studentJsonObject =
													dataObject.get("studentDetails").getAsJsonObject();

											if (studentJsonObject != null) {

												if (studentJsonObject.has("studentName")) {
													studentName = studentJsonObject.get(
															"studentName").getAsString();

													if (studentJsonObject.has("studentEmail")) {
														studentEmail = studentJsonObject.get(
																"studentEmail").getAsString();

														if (studentJsonObject.has("studentPoints")) {
															studentPoints =
																	studentJsonObject.get(
																			"studentPoints").getAsInt();

															if (studentJsonObject.has(
																	"studentCollegeId")) {
																 studentCollegeId =
																		 studentJsonObject.get(
																		 		"studentCollegeId").getAsString();

																 if (studentJsonObject.has(
																 		"studentYearLevelId")) {
																 	studentYearLevelId =
																			studentJsonObject.get("studentYearLevelId").getAsString();

																	 Intent intent = new Intent(RegisterSecondLoading.this, RegisterSuccess.class);
																	 intent.putExtra(IntentExtrasAddresses.INTENT_EXTRA_EMAIL_ADDRESS, emailAddressToPass);
																	 intent.putExtra(IntentExtrasAddresses.INTENT_EXTRA_MOBILE_NUMBER, mobileNumberToPass);
																	 intent.putExtra(IntentExtrasAddresses.INTENT_EXTRA_STUDENTS_ID, studentsIdToPass);
																	 intent.putExtra(IntentExtrasAddresses.INTENT_EXTRA_VERIFICATION_CODE, verificationCodeToPass);
																	 intent.putExtra(IntentExtrasAddresses.INTENT_EXTRA_STUDENT_NAME, studentName);
//																	 intent.putExtra(IntentExtrasAddresses.INTENT_EXTRA_EMAIL_ADDRESS, studentEmail);
																	 intent.putExtra(IntentExtrasAddresses.INTENT_EXTRA_STUDENT_POINTS, studentPoints);
																	 intent.putExtra(IntentExtrasAddresses.INTENT_EXTRA_STUDENT_COLLEGE_ID, studentCollegeId);
																	 intent.putExtra(IntentExtrasAddresses.INTENT_EXTRA_STUDENT_YEAR_LEVEL_ID, studentYearLevelId);

																	 startActivity(intent);

																	 finish();
																 } else {
																	 Toast.makeText(this,
																			 "Student year level " +
																					 "id" +
																					 " not found," +
																			 " " +
																			 "please try again.",	Toast.LENGTH_SHORT).show();
																	 toRegisterSecond();
																 }
															} else {
																Toast.makeText(this, "Student " +
																		"college id" +
																		" not found," +
																		" " +
																		"please try again.",	Toast.LENGTH_SHORT).show();
																toRegisterSecond();
															}
														} else {
															Toast.makeText(this, "Student points " +
																	"not found," +
																	" " +
																	"please try again.",	Toast.LENGTH_SHORT).show();
															toRegisterSecond();
														}
													} else {
														Toast.makeText(this, "Student email not " +
																"found," +
																" " +
																"please try again.",	Toast.LENGTH_SHORT).show();
														toRegisterSecond();
													}
												} else {
													Toast.makeText(this, "Student name not found," +
															" " +
															"please try again.",	Toast.LENGTH_SHORT).show();
													toRegisterSecond();
												}
											} else {
												Toast.makeText(this, "Student information not found, " +
														"please try again.",	Toast.LENGTH_SHORT).show();
												toRegisterSecond();
											}
										} else {
											Toast.makeText(this, "Student information not found, " +
													"please try again.",	Toast.LENGTH_SHORT).show();
											toRegisterSecond();
										}
									} else {
										Toast.makeText(this, "Number code is empty", Toast.LENGTH_SHORT).show();
										toRegisterSecond();
									}
								} else {
									Toast.makeText(this, "student ID is empty", Toast.LENGTH_SHORT).show();
									toRegisterSecond();
								}
							} else {
								Toast.makeText(this, "student mobile number is empty", Toast.LENGTH_SHORT).show();
								toRegisterSecond();
							}
						} else {
							Toast.makeText(this, "student email is empty", Toast.LENGTH_SHORT).show();
							toRegisterSecond();
						}
					} else {
						Toast.makeText(this, "Data is empty", Toast.LENGTH_SHORT).show();
						toRegisterSecond();
					}
				} else {
					Toast.makeText(this, "Code is incorrect", Toast.LENGTH_SHORT).show();
					toRegisterSecond();
				}
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
				Toast.makeText(this, "Code is incorrect", Toast.LENGTH_SHORT).show();
				toRegisterSecond();
			}
		} else {
			Toast.makeText(this, "Error, please try again.", Toast.LENGTH_SHORT).show();
			toRegisterSecond();
		}
	}
	private void toRegisterSecond() {
		Intent intent = new Intent(RegisterSecondLoading.this, RegisterSecond.class);
		intent.putExtra(IntentExtrasAddresses.INTENT_EXTRA_EMAIL_ADDRESS, mEmail);
		intent.putExtra(IntentExtrasAddresses.INTENT_EXTRA_MOBILE_NUMBER, mMobile);
		startActivity(intent);
		finish();
	}
}
