package com.sportcoachhelper;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

/**
 * Created by Claudio on 8/1/13.
 */
public class ActHelpPage extends Activity {

    private TextView modesHelp;
    private TextView modeSelection;
    private TextView modeContinuous;
    private TextView modeDotted;
    private TextView roundPlayerHelp;
    private TextView trianglePlayerHelp;
    private TextView squarePlayerHelp;
    private TextView help_trash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.help);

        modesHelp = (TextView) findViewById(R.id.modesHelp);
        modesHelp.setText(getText(R.string.help_mode_content));

        modeSelection = (TextView) findViewById(R.id.modeSelection);
        modeSelection.setText(getText(R.string.help_mode_selection));

        modeContinuous = (TextView) findViewById(R.id.modeContinuous);
        modeContinuous.setText(getText(R.string.help_mode_continuous));

        modeDotted = (TextView) findViewById(R.id.modeDotted);
        modeDotted.setText(getText(R.string.help_mode_dotted));


        roundPlayerHelp = (TextView) findViewById(R.id.roundPlayerHelp);
        roundPlayerHelp.setText(getText(R.string.help_round_player));

        trianglePlayerHelp = (TextView) findViewById(R.id.trianglePlayerHelp);
        trianglePlayerHelp.setText(getText(R.string.help_triangle_player));

        squarePlayerHelp = (TextView) findViewById(R.id.squarePlayerHelp);
        squarePlayerHelp.setText(getText(R.string.help_square_player));

        help_trash = (TextView) findViewById(R.id.help_trash);
        help_trash.setText(getText(R.string.help_trash_can));
    }
}
